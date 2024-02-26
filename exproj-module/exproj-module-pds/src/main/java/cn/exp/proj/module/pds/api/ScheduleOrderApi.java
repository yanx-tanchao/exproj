package cn.exp.proj.module.pds.api;

import cn.exp.proj.common.web.constant.IWebConstant;
import cn.exp.proj.module.pds.api.dto.PlanOrderInfoDto;
import cn.exp.proj.module.pds.api.dto.TaskMsgDto;
import cn.exp.proj.module.pds.service.IScheduleOrderApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(IWebConstant.REST_API_URL + "/schedule-order")
public class ScheduleOrderApi {
    private IScheduleOrderApiService scheduleOrderApiService;

    /**
     * 算法任务Message保存
     *
     * @param taskMsgDto 算法任务Message
     * @return
     */
    @PostMapping("/rpc-schedule-task-save")
    public void saveTaskMsg(@RequestBody TaskMsgDto taskMsgDto) {
        log.info("算法计算状态接口入参: {}", taskMsgDto);
        scheduleOrderApiService.saveTaskMsg(taskMsgDto);
    }

    @PostMapping("/schedule-order-batch-save")
    public void batchSave(@RequestBody PlanOrderInfoDto planOrderInfoDto) {
        log.info("===========算法保存结果===========");
        scheduleOrderApiService.batchSave(planOrderInfoDto);
    }
}
