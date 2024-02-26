package cn.exp.proj.module.mk.vo;

import cn.exp.proj.module.mk.component.MKWorkCalendar;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @version V1.0
 * @date 2023/4/14 13:26
 **/
@Data
public class MKRunParamVo {
    @ApiModelProperty("计划订单集合map")
    private LinkedHashMap<String, List<MKOrderDtlVo>> orderMap = new LinkedHashMap<>();

    @ApiModelProperty("工作日日历")
    private List<LocalDateTime> calendarAll;

    private MKWorkCalendar mkWorkCalendar;
}
