package cn.exp.proj.module.pds.entity;

import cn.exp.proj.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AlgorithmResultDtlEntity extends BaseEntity<AlgorithmResultDtlEntity> {

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
     * algorithm_schedule_order表id
     */
    private Long scheduleOrderId;

    private Integer learningCurveDay;

    private BigDecimal learningEfficiency;
}
