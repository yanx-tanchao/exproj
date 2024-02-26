package cn.exp.proj.module.pds.api.dto;


import cn.exp.proj.common.core.formatter.TimeDeserializer;
import cn.exp.proj.common.core.formatter.TimeSerializer;
import cn.exp.proj.common.core.util.StringUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("算法消息")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskMsgDto {
    @ApiModelProperty("计划订单号")
    private String messageId;

    @ApiModelProperty("消息创建时间")
    @JsonDeserialize(using = TimeDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    private LocalDateTime messageCreateTime;

    @ApiModelProperty("计算开始时间")
    @JsonDeserialize(using = TimeDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    private LocalDateTime messageCalcTime;

    @ApiModelProperty("1-开始,2-停止,3-取消，2停止需要返回结果")
    private String operationType;

    @ApiModelProperty("任务是否成功:0-代表失败，1-代表成功")
    private Integer resultFlag;

    @ApiModelProperty("解集的数量")
    private Long solutionsSum;

    @Override
    public String toString() {
        return StringUtil.toSnakeJson(this);
    }
}
