package cn.exp.proj.module.pds.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("算法任务数量信息")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskQuantityDto {
    @ApiModelProperty("天数")
    private Integer day;

    @ApiModelProperty("开始天数")
    private Integer startDay;

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("结束天数")
    private Integer endDay;

    @ApiModelProperty("结束时间")
    private Long endTime;

    @ApiModelProperty("天对应排程数量")
    private BigDecimal dayQuantity;

    @ApiModelProperty("天对应排程时间")
    private Integer dayPersistentTime;

    @ApiModelProperty("学习曲线天数")
    private Integer learningCurveDay;

    @ApiModelProperty("学习曲线效率")
    private BigDecimal efficiency;
}
