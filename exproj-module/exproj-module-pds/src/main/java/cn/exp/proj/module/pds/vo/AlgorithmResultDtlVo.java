package cn.exp.proj.module.pds.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AlgorithmResultDtlVo {
    private String taskId;

    private Long scheduleOrderId;

    /**
     * 场景id
     */
    private Long scenarioId;

    /**
     * 日期
     */
    private LocalDate taskDate;

    /**
     * 当天开始时间
     */
    private LocalDateTime startDateTime;

    /**
     * 当天结束时间
     */
    private LocalDateTime endDateTime;

    /**
     * 当天工作时长
     */
    private Integer persistentTime;

    /**
     * 当天生产数量
     */
    private BigDecimal qty;

    /**
     * 学习曲线天数
     */
    private Integer learningCurveDay;

    /**
     * 学习曲线效率
     */
    private BigDecimal learningEfficiency;
}
