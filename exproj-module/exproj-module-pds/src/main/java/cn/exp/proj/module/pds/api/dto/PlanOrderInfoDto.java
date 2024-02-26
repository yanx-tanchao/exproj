package cn.exp.proj.module.pds.api.dto;

import cn.exp.proj.common.core.util.StringUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("算法计划订单汇总")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PlanOrderInfoDto {
    @ApiModelProperty("批量请求的messageId")
    private String messageId;

    @ApiModelProperty("最后一次请求")
    private Boolean lastFlag;

    @ApiModelProperty("计划订单基础信息")
    private List<TaskRelationDto> apOutTaskRelationPool;

    @ApiModelProperty("已排机器任务信息")
    private List<MachineTaskInfoDto> apOutMachineTaskInfoPool;

    @ApiModelProperty("已排任务信息")
    private List<TaskInfoDto> apOutTaskInfoPool;

    @ApiModelProperty("排程目标")
    private TargetDto apTargetPool;

    @ApiModelProperty("未计划任务列表")
    private List<NoScheduleTask> apNoScheduleTaskPool;

    @ApiModelProperty("算法排程任务顺序")
    private List<ScheduleTaskQueueDto> apScheduleTaskIdQueuePool;

    @Override
    public String toString() {
        return StringUtil.toSnakeJson(this);
    }
}
