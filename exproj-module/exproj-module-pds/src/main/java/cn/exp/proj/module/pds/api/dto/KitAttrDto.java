package cn.exp.proj.module.pds.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("算法任务齐套信息")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KitAttrDto {
    @ApiModelProperty("齐套ID")
    private String kittingId;

    @ApiModelProperty("齐套天数")
    private Long kittingDay;

    @ApiModelProperty("齐套数量")
    private BigDecimal kittingQuantity;
}
