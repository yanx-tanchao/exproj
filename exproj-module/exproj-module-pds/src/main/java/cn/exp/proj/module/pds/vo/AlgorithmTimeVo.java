package cn.exp.proj.module.pds.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AlgorithmTimeVo implements ISchduleTask {
    private Integer day;

    private BigDecimal qty;

    private Integer startDay;

    private Long startTime;

    private Integer endDay;

    private Long endTime;

    private Integer persistentTime;

    private Integer learningCurveDay;

    private BigDecimal learningEfficiency;

    /**
     * 当天开始时间
     */
    private LocalDateTime startDateTime;

    /**
     * 当天结束时间
     */
    private LocalDateTime endDateTime;
}
