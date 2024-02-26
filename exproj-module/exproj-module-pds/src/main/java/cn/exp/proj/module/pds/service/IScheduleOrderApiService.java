package cn.exp.proj.module.pds.service;

import cn.exp.proj.module.pds.api.dto.PlanOrderInfoDto;
import cn.exp.proj.module.pds.api.dto.TaskMsgDto;

public interface IScheduleOrderApiService {
    /**
     * 保存算法排出来的结果(分批保存)
     *
     * @param planOrderInfoDto
     */
    PlanOrderInfoDto batchSave(PlanOrderInfoDto planOrderInfoDto);


    /**
     * 修改排程任务
     *
     * @param taskMsgDto
     */
    void saveTaskMsg(TaskMsgDto taskMsgDto);
}
