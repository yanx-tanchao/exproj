package cn.exp.proj.module.pds.entity;

import cn.exp.proj.common.core.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 算法计算结果表
 *
 * @date: 2023-05-23
 */
@Data
public class AlgorithmResultVersionEntity extends BaseEntity<AlgorithmResultVersionEntity> {
    /**
     * 批次消息号
     */
    private Long messageId;

    /**
     * 版本
     */
    private String version;

    /**
     * 场景id
     */
    private Long scenarioId;

    /**
     * 场景名称
     */
    private String scenarioCode;

    /**
     * 场景名称
     */
    private String scenarioName;

    /**
     * 交期惩罚
     */
    private BigDecimal deliverCost;

    /**
     * 效率目标
     */
    private BigDecimal efficientTarget;

    /**
     * 偏好惩罚
     */
    private BigDecimal preferenceCost;

    /**
     * 齐目标惩罚
     */
    private BigDecimal kittingTargetCost;

    /***
     * 综合产值
     */
    private BigDecimal comprehensiveValue;
}
