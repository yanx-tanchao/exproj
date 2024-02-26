package cn.exp.proj.module.pds.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @description MachineTaskInfoQueueDto
 */
@Data
@ApiModel("算法资源任务排程")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MachineTaskInfoStaticDto {

    private List<String> taskIds;
}
