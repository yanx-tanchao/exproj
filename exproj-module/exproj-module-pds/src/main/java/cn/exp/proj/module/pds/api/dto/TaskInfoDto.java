package cn.exp.proj.module.pds.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("算法已排任务")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskInfoDto {
    @ApiModelProperty("任务ID")
    private String taskId;

    @ApiModelProperty("工艺段编码")
    private String prdProcessCode;

    @ApiModelProperty("机器编码")
    private String machineCode;

    @ApiModelProperty("是否超期")
    private Boolean overDeliverFlag;

    @ApiModelProperty("任务开始时间")
    private Integer taskStartDay;

    @ApiModelProperty("任务数量")
    private BigDecimal taskQuantity;

    @ApiModelProperty("任务工作时长")
    private Integer taskPersistentTime;

    @ApiModelProperty("标准工时")
    private BigDecimal standardTime;

    @ApiModelProperty("每天对应的排程数量")
    private List<TaskQuantityDto> taskQuantityPool;

    @ApiModelProperty("齐套信息")
    private List<KitAttrDto> kittingAttrPool;

    @ApiModelProperty("//1-制造任务，2-转款任务，3-称重任务")
    private Integer taskType;

    @ApiModelProperty("班次")
    private Long taskStartTime;

    @ApiModelProperty("结束天")
    private Integer taskEndDay;

    @ApiModelProperty("结束班次")
    private Long taskEndTime;
}
