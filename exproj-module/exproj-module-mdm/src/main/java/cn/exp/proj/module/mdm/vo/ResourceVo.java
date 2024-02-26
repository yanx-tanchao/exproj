package cn.exp.proj.module.mdm.vo;

import cn.exp.proj.common.core.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResourceVo extends BaseVo {
    @ApiModelProperty("资源编码")
    private String resourceCode;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("资源类别")
    private String resourceType;

    @ApiModelProperty("制造效率")
    private BigDecimal efficiency;
}
