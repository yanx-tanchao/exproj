package cn.exp.proj.module.pds.mapper;

import cn.exp.proj.common.core.converter.IMapStruct;
import cn.exp.proj.common.core.util.TimeUtil;
import cn.exp.proj.module.pds.api.dto.NoScheduleTask;
import cn.exp.proj.module.pds.api.dto.TaskInfoDto;
import cn.exp.proj.module.pds.api.dto.TaskQuantityDto;
import cn.exp.proj.module.pds.api.dto.TaskRelationDto;
import cn.exp.proj.module.pds.entity.AlgorithmResultEntity;
import cn.exp.proj.module.pds.vo.AlgorithmResultVo;
import cn.exp.proj.module.pds.vo.AlgorithmTimeVo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", imports = {TimeUtil.class}, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IAlgorithmResultMapper extends IMapStruct<AlgorithmResultEntity, AlgorithmResultVo> {
    AlgorithmResultVo newAlgorithmResult(AlgorithmResultVo algorithmResultVo);

    @Mapping(target = "mkOrder", source = "taskRelation.taskId")
    @Mapping(target = "taskId", source = "outTaskInfo.taskId")
    @Mapping(target = "originMkTask", source = "taskRelation.taskId")
    @Mapping(target = "qty", source = "outTaskInfo.taskQuantity")
    @Mapping(target = "resourceCode", source = "outTaskInfo.machineCode")
    @Mapping(target = "persistentTime", source = "outTaskInfo.taskPersistentTime")
    @Mapping(target = "prdProcess", source = "outTaskInfo.prdProcessCode")
    @Mapping(target = "algorithmTimes", source = "outTaskInfo.taskQuantityPool", qualifiedByName = {"toTaskTimes"})
    AlgorithmResultVo toAlgorithmResult(TaskRelationDto taskRelation, TaskInfoDto outTaskInfo);

    @Mapping(target = "mkOrder", source = "taskRelation.taskId")
    @Mapping(target = "originMkTask", source = "taskRelation.taskId")
    @Mapping(target = "taskId", source = "outTaskInfo.taskId")
    @Mapping(target = "qty", source = "outTaskInfo.quantity")
    AlgorithmResultVo toAlgorithmResult(TaskRelationDto taskRelation, NoScheduleTask outTaskInfo);

    @Named("toTaskTimes")
    @IterableMapping(qualifiedByName = "toTaskTime")
    List<AlgorithmTimeVo> toTaskTimes(List<TaskQuantityDto> quantityDto);

    @Named("toTaskTime")
    @Mapping(target = "qty", source = "dayQuantity")
    @Mapping(target = "persistentTime", source = "dayPersistentTime")
    @Mapping(target = "learningEfficiency", source = "efficiency")
    AlgorithmTimeVo toTaskTime(TaskQuantityDto quantityDto);
}
