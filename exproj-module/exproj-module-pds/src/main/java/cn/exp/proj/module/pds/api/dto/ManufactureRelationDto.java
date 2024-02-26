package cn.exp.proj.module.pds.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/***
 * @description ManufactureRelationDto
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ManufactureRelationDto {
    @ApiModelProperty("当前任务")
    private String taskId;

    @ApiModelProperty("制造任务")
    private String manufactureTaskId;
}
