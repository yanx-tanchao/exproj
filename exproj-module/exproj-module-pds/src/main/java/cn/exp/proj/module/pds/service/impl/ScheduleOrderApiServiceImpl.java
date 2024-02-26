package cn.exp.proj.module.pds.service.impl;

import cn.exp.proj.common.core.asserts.ProjAssert;
import cn.exp.proj.common.core.constants.IConstant;
import cn.exp.proj.common.core.util.CollectionUtil;
import cn.exp.proj.common.core.util.StringUtil;
import cn.exp.proj.common.core.util.TimeUtil;
import cn.exp.proj.common.core.wrapper.BooleanWrapper;
import cn.exp.proj.module.pds.api.dto.MachineTaskInfoDto;
import cn.exp.proj.module.pds.api.dto.MachineTaskInfoDynamicDto;
import cn.exp.proj.module.pds.api.dto.MachineTaskInfoDynamicPieceDto;
import cn.exp.proj.module.pds.api.dto.MachineTaskInfoStaticDto;
import cn.exp.proj.module.pds.api.dto.NoScheduleTask;
import cn.exp.proj.module.pds.api.dto.PlanOrderInfoDto;
import cn.exp.proj.module.pds.api.dto.TaskInfoDto;
import cn.exp.proj.module.pds.api.dto.TaskMsgDto;
import cn.exp.proj.module.pds.api.dto.TaskRelationDto;
import cn.exp.proj.module.pds.dao.IMdmResourceDao;
import cn.exp.proj.module.pds.dao.IScheduleTaskDao;
import cn.exp.proj.module.pds.entity.AlgorithmResultDtlEntity;
import cn.exp.proj.module.pds.entity.AlgorithmResultEntity;
import cn.exp.proj.module.pds.entity.AlgorithmResultVersionEntity;
import cn.exp.proj.module.pds.entity.ResourceEntity;
import cn.exp.proj.module.pds.entity.ScheduleTaskEntity;
import cn.exp.proj.module.pds.mapper.IAlgorithmResultDtlMapper;
import cn.exp.proj.module.pds.mapper.IAlgorithmResultMapper;
import cn.exp.proj.module.pds.mapper.IAlgorithmResultVersionMapper;
import cn.exp.proj.module.pds.service.IAlgorithmResultDtlService;
import cn.exp.proj.module.pds.service.IAlgorithmResultService;
import cn.exp.proj.module.pds.service.IAlgorithmResultVersionService;
import cn.exp.proj.module.pds.service.IScheduleOrderApiService;
import cn.exp.proj.module.pds.vo.AlgorithmResultDtlVo;
import cn.exp.proj.module.pds.vo.AlgorithmResultVo;
import cn.exp.proj.module.pds.vo.AlgorithmTimeVo;
import cn.exp.proj.module.pds.vo.ISchduleTask;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ScheduleOrderApiServiceImpl implements IScheduleOrderApiService {
    private IAlgorithmResultMapper iMapper;

    private IAlgorithmResultVersionMapper iVersonMapper;

    private IAlgorithmResultDtlMapper iDtlMapper;

    private IAlgorithmResultService algorithmScheduleOrderService;

    private IAlgorithmResultVersionService algorithmScheduleVersionService;

    private IAlgorithmResultDtlService algorithmScheduleOrderDetailService;

    private IScheduleTaskDao iScheduleTaskDao;

    private IMdmResourceDao iMdmResourceDao;

    @Override
    public PlanOrderInfoDto batchSave(PlanOrderInfoDto dto) {
        // 拆单关系
        ProjAssert.nonNull(dto.getApOutTaskRelationPool());
        ScheduleTaskEntity taskEntity = findAndSaveRpcTask(dto);

        // machine-task
        List<MachineTaskInfoDto> machineTasks = dto.getApOutMachineTaskInfoPool();

        // 获取所有的Task - 含每天的数据
        TaskResult allTasks = getAllTasks(dto);

        // 查询资源, 并查询资源对应的日历
        Map<String, ResourceEntity> resourceMap = loadAllResources(machineTasks);

        // 动态组别(Printing)分配设备
        matchDynamicMachine(allTasks.taskMap, machineTasks, resourceMap);
        MachineTime machineTime = getMachineTimes(taskEntity, resourceMap);
        AlgorithmResult algorithmResult = loadPlanTask(allTasks.taskMap, machineTime);

        // 所有要入库的排程订单和排程任务
        List<AlgorithmResultVo> tasks = CollectionUtil.orElse(algorithmResult.tasks());

        // 查询Task表, 获取TaskId和MkOrderId
        log.info("补充设备Id和MKOrderId...");
        tasks.addAll(allTasks.noScheduledTasks);

        // 先补充所有已排订单的资源ID和MKOrderId, 避免和未排任务混在一起冗余计算
        for (AlgorithmResultVo task : tasks) {
            ResourceEntity resourceEntity = resourceMap.get(task.getResourceCode());
            Optional.ofNullable(resourceEntity).ifPresent(resource -> task.setResourceId(resource.getId()));
        }

        if (CollectionUtil.isEmpty(tasks)) {
            log.warn("没有可处理的任务...");
            return dto;
        }

        // 保存数据
        save(dto, tasks, taskEntity, algorithmResult.dailyTasks());

        log.info("算法解集保存完成...");
        return dto;
    }

    private void save(PlanOrderInfoDto dto, List<AlgorithmResultVo> tasks, ScheduleTaskEntity rpcTask,
                      List<AlgorithmResultDtlVo> dailyTasks) {
        log.info("保存数据...");

        // 补足并保存数据
        String version = saveVersion(dto, rpcTask);
        List<AlgorithmResultEntity> savedOrders = saveTasks(tasks, rpcTask, version);
        var taskOrderMap = CollectionUtil.group(dailyTasks, AlgorithmResultDtlVo::getTaskId);
        for (AlgorithmResultEntity scheduleOrder : savedOrders) {
            List<AlgorithmResultDtlVo> matchedTasks = taskOrderMap.get(scheduleOrder.getTaskId());
            if (CollectionUtil.isNotEmpty(matchedTasks)) {
                matchedTasks.forEach(task -> task.setScheduleOrderId(scheduleOrder.getId()));
            }
        }
        saveDailyTasks(dailyTasks, rpcTask);
    }

    private ScheduleTaskEntity findAndSaveRpcTask(PlanOrderInfoDto dto) {
        // 排程任务表
        LambdaQueryWrapper<ScheduleTaskEntity> taskWrapper = Wrappers.lambdaQuery();
        taskWrapper.eq(ScheduleTaskEntity::getId, dto.getMessageId());
        taskWrapper.eq(ScheduleTaskEntity::getDeleted, Boolean.FALSE);
        ScheduleTaskEntity taskEntity = iScheduleTaskDao.selectOne(taskWrapper);

        // 当前无任务或任务状态!=运行中，放弃后续操作
        ProjAssert.nonNull(taskEntity, 10000, "排程任务不存在");

        // 更新任务状态
        saveRpcTask(dto, taskEntity);

        return taskEntity;
    }

    private Map<String, List<String>> machineGroupRelation(List<MachineTaskInfoDto> machineTasks) {
        // 资源组 printing
        Set<String> machineGroups = machineTasks.stream().filter(v -> "1".equals(v.getDynamicPieceFlag())).map(g -> g.getMachineCode()).collect(Collectors.toSet());

        LambdaQueryWrapper<ResourceEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ResourceEntity::getResourcegroupCode, machineGroups);
        List<ResourceEntity> list = iMdmResourceDao.selectList(wrapper);
        Map<String, List<String>> resultMap = CollectionUtil.groupAndThen(list, ResourceEntity::getResourcegroupCode, listMap -> {
            Map<String, List<String>> map = new HashMap<>();
            listMap.forEach((k, v) -> map.put(k, CollectionUtil.map(v, ResourceEntity::getResourceCode)));
            return map;
        });

        return resultMap;
    }

    private void saveRpcTask(PlanOrderInfoDto dto, ScheduleTaskEntity taskEntity) {
        BooleanWrapper lastFlag = BooleanWrapper.of(dto.getLastFlag());
        if (!lastFlag.eval()) {
            return;
        }

        // 如果是一次排程的最后一版， 数据保存后， 更新message状态
        taskEntity.setRunResult(IConstant.ResultEnum.SUCCESS.getCode());
        Duration duration = Duration.between(taskEntity.getRunStartTime(), LocalDateTime.now());
        long m = duration.toMinutes();
        taskEntity.setRunTime(String.valueOf(m));
        taskEntity.setRunTimeRadio("100");
        iScheduleTaskDao.updateById(taskEntity);
        log.info("Msg:[{}]已接收所有结果并生成版本号入库...", dto.getMessageId());
    }

    private Map<String, ResourceEntity> loadAllResources(List<MachineTaskInfoDto> machineTasks) {
        log.info("查询所有可用设备...");
        // 获取所有资源
        Set<String> resourceSets = CollectionUtil.newHashSet();
        Set<String> resourceGroup = CollectionUtil.newHashSet();
        for (MachineTaskInfoDto machineTask : machineTasks) {
            if ("1".equals(machineTask.getDynamicPieceFlag())) {
                resourceGroup.add(machineTask.getMachineCode());
                continue;
            }
            resourceSets.add(machineTask.getMachineCode());
        }
        if (CollectionUtil.isEmpty(resourceSets) && CollectionUtil.isEmpty(resourceGroup)) {
            log.warn("无资源, 请检查入参或忽略...");
            return CollectionUtil.newHashMap();
        }

        LambdaQueryWrapper<ResourceEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.and((wrapper1) -> {
            wrapper1.in(ResourceEntity::getResourceCode, resourceSets);
            wrapper1.or();
            wrapper1.in(CollectionUtil.isNotEmpty(resourceGroup), ResourceEntity::getResourcegroupCode, resourceGroup);
        });
        return CollectionUtil.toMap(iMdmResourceDao.selectList(wrapper), ResourceEntity::getResourceCode);
    }

    /**
     * 按照Machine-Task的顺序对Task进行排序
     *
     * @param allTasks         所有任务
     * @param machineTaskInfos 机器上的任务顺序
     * @param resourceMap
     */
    private void matchDynamicMachine(Map<String, List<AlgorithmResultVo>> allTasks, List<MachineTaskInfoDto> machineTaskInfos,
                                     Map<String, ResourceEntity> resourceMap) {
        log.info("处理动态组别的设备...");
        if (CollectionUtil.isEmpty(allTasks)) {
            log.warn("未找到已排程任务.");
            return;
        }
        if (CollectionUtil.isEmpty(machineTaskInfos)) {
            log.warn("ap_out_machine_task_info_pool缺失, 无法确定动态组别, 可能导致排程结果异常.");
            return;
        }
        Map<String, List<ResourceEntity>> grMap = CollectionUtil.group(resourceMap.values(), mdmResource -> {
            return Optional.ofNullable(mdmResource.getResourcegroupCode()).orElse(StringUtil.EMPTY);
        });
        for (MachineTaskInfoDto machineTaskInfo : machineTaskInfos) {
            if (1 != machineTaskInfo.getDynamicPieceFlag()) {
                continue;
            }
            String machine = machineTaskInfo.getMachineCode();
            List<AlgorithmResultVo> algorithmResultVos = allTasks.get(machine);
            if (CollectionUtil.isEmpty(algorithmResultVos)) {
                continue;
            }

            // 按照设备组分配详细设备
            List<ResourceEntity> resources = grMap.get(machine);
            Map<String, String> taskMachineMap = pieceMachine(machineTaskInfo.getDynamicTaskIdsQueue(), resources);
            for (AlgorithmResultVo orderVo : algorithmResultVos) {
                String taskMachine = taskMachineMap.get(orderVo.getTaskId());
                if (StringUtil.isBlank(taskMachine)) {
                    continue;
                }
                orderVo.setResourceCode(taskMachine);
                allTasks.compute(taskMachine, (k, v) -> CollectionUtil.orAdd(v, orderVo));
            }

            // 清理原设备组数据
            allTasks.remove(machine);
        }
    }

    /**
     * 分组设备
     *
     * @param taskInfos
     * @param resources
     * @return
     */
    private Map<String, String> pieceMachine(List<MachineTaskInfoDynamicDto> taskInfos, List<ResourceEntity> resources) {
        Map<String, String> taskMachineMap = CollectionUtil.newHashMap();
        for (MachineTaskInfoDynamicDto taskInfo : taskInfos) {
            // 设备总分组数量
            Integer pieceType = taskInfo.getPieceType();
            List<ResourceEntity> allPieceResources = CollectionUtil.limit(resources, pieceType);
            List<MachineTaskInfoDynamicPieceDto> pieceTasks = taskInfo.getPieceTaskIdsPool();

            // 按总分组和子分组数量一组一组的使用设备
            int machineFinalIndex = 0;
            for (MachineTaskInfoDynamicPieceDto pieceTask : pieceTasks) {
                List<ResourceEntity> pieceResources = CollectionUtil.newArrayList();
                Integer subPieceType = pieceTask.getSubPieceType();
                for (Integer i = 0; i < subPieceType; i++) {
                    int machineIndex = machineFinalIndex % pieceType;
                    pieceResources.add(allPieceResources.get(machineIndex));
                    machineFinalIndex++;
                }
                List<MachineTaskInfoStaticDto> taskIdsPool = pieceTask.getTaskIdsPool();
                for (int i = 0; i < taskIdsPool.size(); i++) {
                    MachineTaskInfoStaticDto staticDto = taskIdsPool.get(i);
                    ResourceEntity resourceEntity = pieceResources.get(i);
                    List<String> taskIds = staticDto.getTaskIds();
                    if (CollectionUtil.isEmpty(taskIds)) {
                        continue;
                    }
                    taskIds.forEach(taskId -> taskMachineMap.put(taskId, resourceEntity.getResourceCode()));
                }
            }

        }
        return taskMachineMap;
    }

    /**
     * 按照动态组别信息 处理
     * 2023-09-25
     */
    private List<String> taskQueue(MachineTaskInfoDto machineTaskInfo) {
        // 动态组别信息，0-静态组别，1-动态组别
        if ("0".equals(machineTaskInfo.getDynamicPieceFlag())) {
            List<MachineTaskInfoStaticDto> staticTaskIds = machineTaskInfo.getStaticTaskIdsQueue();
            return CollectionUtil.flatMap(staticTaskIds, MachineTaskInfoStaticDto::getTaskIds);
        }

        List<MachineTaskInfoDynamicDto> dynamicTaskIdsQueue = machineTaskInfo.getDynamicTaskIdsQueue();
        if (CollectionUtil.isEmpty(dynamicTaskIdsQueue)) {
            return CollectionUtil.newArrayList();
        }

        List<String> taskIdQueue = CollectionUtil.newArrayList();
        for (MachineTaskInfoDynamicDto dynamicDto : dynamicTaskIdsQueue) {
            List<MachineTaskInfoDynamicPieceDto> pieceTaskIdsPool = dynamicDto.getPieceTaskIdsPool();
            List<MachineTaskInfoStaticDto> taskIdsPool = CollectionUtil.flatMap(pieceTaskIdsPool, pieceDto -> {
                return pieceDto.getTaskIdsPool();
            });
            taskIdQueue.addAll(CollectionUtil.flatMap(taskIdsPool, MachineTaskInfoStaticDto::getTaskIds));
        }

        return taskIdQueue;
    }


    /**
     * 获取所有排程任务, 并赋值mkOrder
     *
     * @param taskRelations       排程任务拆分信息 - 含原订单
     * @param taskMap             排程任务明细
     * @param nonScheduledTaskMap
     * @return
     */
    private TaskResult getAllTasks(PlanOrderInfoDto dto) {
        log.info("处理所有已排和未排任务...");
        List<TaskRelationDto> taskRelations = dto.getApOutTaskRelationPool();
        List<TaskInfoDto> taskInfoPool = dto.getApOutTaskInfoPool();
        Map<String, TaskInfoDto> taskMap = CollectionUtil.toMap(taskInfoPool, TaskInfoDto::getTaskId);
        Map<String, NoScheduleTask> nonScheduledTaskMap = CollectionUtil.toMap(dto.getApNoScheduleTaskPool(), NoScheduleTask::getTaskId);

        Map<String, List<AlgorithmResultVo>> planTaskMap = new HashMap<>();
        List<AlgorithmResultVo> noScheduleTask = new ArrayList<>();
        if (CollectionUtil.isEmpty(taskRelations)) {
            return new TaskResult(noScheduleTask, planTaskMap);
        }
        for (TaskRelationDto taskRelationDto : taskRelations) {
            List<String> taskIdPool = taskRelationDto.getTaskIdPool();
            List<TaskInfoDto> taskInfoDtos = CollectionUtil.map(taskIdPool, taskMap::get);
            List<NoScheduleTask> noScheduleTasks = CollectionUtil.map(taskIdPool, nonScheduledTaskMap::get);
            if (CollectionUtil.isEmpty(taskInfoDtos) && CollectionUtil.isEmpty(noScheduleTasks)) {
                continue;
            }

            for (TaskInfoDto taskInfo : taskInfoDtos) {
                // 2023-09-25 添加任务过滤（algorithm_schedule_order只存储制造任务）   1-制造任务，2-转款任务，3-称重任务
                if (1 != taskInfo.getTaskType()) {
                    continue;
                }
                AlgorithmResultVo orderVo = iMapper.toAlgorithmResult(taskRelationDto, taskInfo);
                if (Objects.isNull(orderVo)) {
                    continue;
                }
                planTaskMap.compute(taskInfo.getMachineCode(), (k, v) -> CollectionUtil.orAdd(v, orderVo));
            }

            for (NoScheduleTask task : noScheduleTasks) {
                AlgorithmResultVo orderVo = iMapper.toAlgorithmResult(taskRelationDto, task);
                if (Objects.isNull(orderVo)) {
                    continue;
                }
                noScheduleTask.add(orderVo);
            }
        }
        return new TaskResult(noScheduleTask, planTaskMap);
    }

    /**
     * 获取所有任务
     *
     * @param allTasks    按资源分组的所有任务
     * @param machineTime 设备工作时间
     * @return 所有任务
     */
    private AlgorithmResult loadPlanTask(Map<String, List<AlgorithmResultVo>> allTasks, MachineTime machineTime) {
        log.info("处理每个任务的开始结束时间...");
        List<AlgorithmResultVo> allPlanTasks = new ArrayList<>();
        List<AlgorithmResultDtlVo> detailVos = new ArrayList<>();
        Map<String, TreeMap<LocalDate, List<WorkTime>>> machineTimes = machineTime.machineTimeMap();
        if (CollectionUtil.isEmpty(allTasks) || CollectionUtil.isEmpty(machineTimes)) {
            return new AlgorithmResult(allPlanTasks, detailVos);
        }

        for (Map.Entry<String, List<AlgorithmResultVo>> machineTaskEntry : allTasks.entrySet()) {
            String machine = machineTaskEntry.getKey();
            TreeMap<LocalDate, List<WorkTime>> allTimeMap = machineTimes.get(machine);
            if (CollectionUtil.isEmpty(allTimeMap)) {
                continue;
            }

            // 1. 取每天的第一个班次为当天的开始班次
            List<WorkTime> workTimes = CollectionUtil.map(allTimeMap.values(), CollectionUtil::first);

            // 2. 取班次开始时间为排产开始时间
            List<LocalDateTime> avaliableTime = CollectionUtil.map(workTimes, WorkTime::time);
            if (CollectionUtil.isEmpty(avaliableTime)) {
                continue;
            }

            // 计算并补足任务的起止时间
            List<AlgorithmResultVo> orderVos = machineTaskEntry.getValue();
            AlgorithmResult result = fillTaskTime(orderVos, avaliableTime);

            allPlanTasks.addAll(result.tasks());
            detailVos.addAll(result.dailyTasks());
        }
        return new AlgorithmResult(allPlanTasks, detailVos);
    }

    private MachineTime getMachineTimes(ScheduleTaskEntity taskEntity, Map<String, ResourceEntity> resourceMap) {
        log.info("获取设备工作日历...");
        List<Long> machines = CollectionUtil.map(resourceMap.values(), ResourceEntity::getId);
        Map<String, TreeMap<LocalDate, List<WorkTime>>> machineTimeMap = new LinkedHashMap<>();
        if (CollectionUtil.isEmpty(machines)) {
            log.warn("未匹配到系统中已维护资源, 请检查资源数据.");
            return new MachineTime(machineTimeMap);
        }

        return new MachineTime(machineTimeMap);
    }

    record WorkTime(LocalDateTime time, Boolean workDay) {
    }

    record MachineTime(Map<String, TreeMap<LocalDate, List<WorkTime>>> machineTimeMap) {
    }

    record AlgorithmResult(List<AlgorithmResultVo> tasks, List<AlgorithmResultDtlVo> dailyTasks) {
    }

    record TaskResult(List<AlgorithmResultVo> noScheduledTasks, Map<String, List<AlgorithmResultVo>> taskMap) {
    }

    /**
     * 计算资源上每个任务每天的起止时间
     *
     * @param orderVos      资源上的所有任务
     * @param avaliableTime 资源可用工作时间
     * @param timeline      日历计算器
     */
    private AlgorithmResult fillTaskTime(List<AlgorithmResultVo> orderVos, List<LocalDateTime> avaliableTime) {
        LocalDateTime startTime = CollectionUtil.first(avaliableTime);
        List<AlgorithmResultDtlVo> dailyTaskVos = new ArrayList<>();
        List<AlgorithmResultVo> taskVos = new ArrayList<>();
        for (AlgorithmResultVo orderVo : orderVos) {
            // 计算甘特图任务条
            AlgorithmResultVo scheduleOrderVo = iMapper.newAlgorithmResult(orderVo);
            dealTaskTime(avaliableTime, scheduleOrderVo);
            taskVos.add(scheduleOrderVo);

            // 计算每日任务详情
            List<AlgorithmResultDtlVo> dailyResult = getDailyTasks(avaliableTime, orderVo);
            dailyTaskVos.addAll(dailyResult);
        }
        return new AlgorithmResult(taskVos, dailyTaskVos);
    }

    private List<AlgorithmResultDtlVo> getDailyTasks(List<LocalDateTime> avaliableTime, AlgorithmResultVo orderVo) {
        List<AlgorithmResultDtlVo> detailVos = new ArrayList<>();
        List<AlgorithmTimeVo> planTaskTimes = orderVo.getAlgorithmTimes();
        if (CollectionUtil.isEmpty(planTaskTimes)) {
            return detailVos;
        }

        // Task每天的工作时间
        for (AlgorithmTimeVo algorithmTime : planTaskTimes) {
            // 每日的Task
            dealTaskTime(avaliableTime, algorithmTime);
            AlgorithmResultDtlVo detailVo = iDtlMapper.toVo(algorithmTime);
            detailVo.setTaskId(orderVo.getTaskId());
            detailVos.add(detailVo);
        }
        return detailVos;
    }


    private <T extends ISchduleTask> void dealTaskTime(List<LocalDateTime> avaliableTime, T task) {
        int dateIndex = task.getStartDay() - 1;
        if (dateIndex >= avaliableTime.size()) {
            // 如果超出可用时间范围, 保证数据全部入库, 即使入库数据异常
            dateIndex = avaliableTime.size() - 1;
        }

        // 计算开始时间
        LocalDateTime workDateTime = avaliableTime.get(dateIndex);
        LocalDate workDay = TimeUtil.toLocalDate(workDateTime);
        LocalTime taskStartTime = LocalTime.ofSecondOfDay(task.getStartTime());
        LocalDateTime startTime = LocalDateTime.of(workDay, taskStartTime);
        task.setStartDateTime(startTime);

        // 计算结束时间
        LocalDateTime workEndTime = workDateTime;
        int dateEndIndex = task.getEndDay() - 1;
        if (dateEndIndex >= avaliableTime.size()) {
            // 如果超出可用时间范围, 保证数据全部入库, 即使入库数据异常
            dateEndIndex = avaliableTime.size() - 1;
        }
        if (dateEndIndex != dateIndex) {
            workEndTime = avaliableTime.get(dateEndIndex);
        }

        LocalDate workEndDay = TimeUtil.toLocalDate(workEndTime);
        LocalTime taskEndTime = LocalTime.ofSecondOfDay(task.getEndTime());
        LocalDateTime endTime = LocalDateTime.of(workEndDay, taskEndTime);
        task.setEndDateTime(endTime);
    }

    private String saveVersion(PlanOrderInfoDto dto, ScheduleTaskEntity taskEntity) {
        // fill批次号, 生成版本号
        String version = System.currentTimeMillis() + StringUtil.EMPTY;
        Long dataId = taskEntity.getDataId();

        // 保存版本主表数据
        AlgorithmResultVersionEntity versionEntity = iVersonMapper.toEntity(dto, dto.getApTargetPool());
        versionEntity.setScenarioId(dataId);
        versionEntity.setVersion(version);
        iVersonMapper.fillBaseField(taskEntity, versionEntity);
        iVersonMapper.fillUserInfo(taskEntity, versionEntity);
        algorithmScheduleVersionService.save(versionEntity);
        return version;
    }

    private List<AlgorithmResultEntity> saveTasks(List<AlgorithmResultVo> orderVos,
                                                  ScheduleTaskEntity taskEntity, String version) {
        List<AlgorithmResultEntity> planTaskEntities = CollectionUtil.map(orderVos, orderVo -> {
            AlgorithmResultEntity orderEntity = iMapper.toEntity(orderVo);
            orderEntity.setScenarioId(taskEntity.getDataId());
            orderEntity.setMessageId(taskEntity.getId());
            orderEntity.setVersion(version);
            iMapper.fillBaseField(taskEntity, orderEntity);
            iMapper.fillUserInfo(taskEntity, orderEntity);
            return orderEntity;
        });

        algorithmScheduleOrderService.saveBatch(planTaskEntities, 20000);
        return planTaskEntities;
    }

    private void saveDailyTasks(List<AlgorithmResultDtlVo> tasks, ScheduleTaskEntity taskEntity) {
        if (CollectionUtil.isEmpty(tasks)) {
            return;
        }

        List<AlgorithmResultDtlEntity> planTaskEntities = CollectionUtil.map(tasks, order -> {
            AlgorithmResultDtlEntity orderDetailEntity = iDtlMapper.toEntity(order);
            orderDetailEntity.setTaskDate(TimeUtil.toLocalDate(order.getStartDateTime()));
            orderDetailEntity.setScenarioId(taskEntity.getDataId());
            iMapper.fillBaseField(taskEntity, orderDetailEntity);
            iMapper.fillUserInfo(taskEntity, orderDetailEntity);
            return orderDetailEntity;
        });

        algorithmScheduleOrderDetailService.saveBatch(planTaskEntities, 20000);
    }

    @Override
    public void saveTaskMsg(TaskMsgDto taskMsgDto) {
        LambdaQueryWrapper<ScheduleTaskEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ScheduleTaskEntity::getId, taskMsgDto.getMessageId());
        queryWrapper.eq(ScheduleTaskEntity::getDeleted, false);
        ScheduleTaskEntity taskEntity = iScheduleTaskDao.selectOne(queryWrapper);

        ProjAssert.nonNull(taskEntity, 10000, "排程任务不存在");

        // 算法排程成功，新的排程结果即将入库， 按场景删除历史数据
        Long dataId = taskEntity.getDataId();
        QueryWrapper wrapper = Wrappers.query();
        wrapper.eq("scenario_id", dataId);
        wrapper.eq("tenant_id", taskEntity.getTenantId());

        // 1.删除排程版本
        algorithmScheduleVersionService.remove(wrapper);

        // 2.删除排程结果
        algorithmScheduleOrderService.remove(wrapper);

        // 3.删除排程结果每日明细
        algorithmScheduleOrderDetailService.remove(wrapper);
    }
}
