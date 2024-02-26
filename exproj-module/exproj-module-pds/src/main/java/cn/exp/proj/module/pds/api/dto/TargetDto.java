package cn.exp.proj.module.pds.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("排程目标")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TargetDto {

    @ApiModelProperty("偏好惩罚")
    private BigDecimal apPreferenceCost;

    @ApiModelProperty("齐目标惩罚")
    private BigDecimal apKittingTargetCost;

    @ApiModelProperty("综合产值")
    private BigDecimal apComprehensiveValue;

}
