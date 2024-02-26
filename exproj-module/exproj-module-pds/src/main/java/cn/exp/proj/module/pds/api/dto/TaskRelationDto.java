package cn.exp.proj.module.pds.api.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("算法计划订单")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskRelationDto {
    @ApiModelProperty("计划订单号")
    private String taskId;

    @ApiModelProperty("任务ID")
    private List<String> taskIdPool;
}
