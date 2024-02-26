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
@ApiModel("算法未计划任务")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NoScheduleTask {
    @ApiModelProperty("任务ID")
    private String taskId;

    @ApiModelProperty("任务数量")
    private BigDecimal quantity;

    @ApiModelProperty("齐套信息")
    private List<KitAttrDto> kittingAttrPool;

    @ApiModelProperty("工段")
    private String prdProcessCode;
}
