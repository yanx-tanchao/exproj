package cn.exp.proj.module.pds.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("算法资源任务排程")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MachineTaskInfoDto {
    @ApiModelProperty("机器号")
    private String machineCode;

    @ApiModelProperty("工段")
    private String prdProcessCode;

    @ApiModelProperty("动态组别信息")
    private Integer dynamicPieceFlag;

    @ApiModelProperty("静态")
    private List<MachineTaskInfoStaticDto> staticTaskIdsQueue;

    @ApiModelProperty("动态")
    private List<MachineTaskInfoDynamicDto> dynamicTaskIdsQueue;
}
