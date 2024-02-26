package cn.exp.proj.module.pds.entity;

import cn.exp.proj.common.core.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 排程订单明细表(HgApsScheduleOrderDetail)实体类
 *
 * 
 * @date: 2023-05-26 14:17:59
 */
@Data
public class ScheduleOrderDtlEntity extends BaseEntity<ScheduleOrderDtlEntity> implements Serializable {
    /**
     * 场景id
     */
    private Long scenarioId;

    /**
     * 排程订单表ID
     */
    private Long apsScheduleOrderId;

    /**
     * 资源id
     */
    private Long resourceId;

    /**
     * 资源code
     */
    private String resourceCode;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务日期
     */
    private LocalDate taskDate;

    /**
     * 开始时间
     */
    private LocalDateTime startDateTime;

    /**
     * 结束时间
     */
    private LocalDateTime endDateTime;

    /**
     * 生产时长 单位秒
     */
    private Long persistentTime;

    /**
     * 数量
     */
    private Double qty;

    /**
     * 效率
     */
    private BigDecimal efficiency;
}

