package cn.exp.proj.module.pds.api.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 */
@Data
@ApiModel("算法排程任务顺序")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduleTaskQueueDto {
    @ApiModelProperty("工段")
    private String prdProcessCode;

    @ApiModelProperty("任务列表")
    private List<String> taskIdQueue;
}
