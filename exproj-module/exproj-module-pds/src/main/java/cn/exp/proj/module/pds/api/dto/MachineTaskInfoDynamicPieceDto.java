package cn.exp.proj.module.pds.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description MachineTaskInfoDynamicPieceDto
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MachineTaskInfoDynamicPieceDto {
    @ApiModelProperty("子分组信息")
    private Integer subPieceType;

    @ApiModelProperty("子分片信息")
    private Integer subPieceNum;

    @ApiModelProperty("排程任务")
    private List<MachineTaskInfoStaticDto> taskIdsPool;
}
