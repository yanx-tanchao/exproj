package cn.exp.proj.module.pds.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description MachineTaskInfoDynamicDto
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MachineTaskInfoDynamicDto {
    @ApiModelProperty("转款任务")
    private List<String> changeoverTaskIdPool;

    @ApiModelProperty("分组信息")
    private Integer pieceType;

    @ApiModelProperty("分组信息")
    private List<MachineTaskInfoDynamicPieceDto> pieceTaskIdsPool;
}
