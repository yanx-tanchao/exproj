package cn.exp.proj.module.pds.entity;

import cn.exp.proj.common.core.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 排程任务表
 */
@Data
public class ScheduleTaskEntity extends BaseEntity<ScheduleTaskEntity> {
    /**
     * 数据类型 1版本 2场景
     */
    private String dataType;

    /**
     * 数据id
     */
    private Long dataId;

    /**
     * 任务状态 0 未执行 1 执行中 2 已完成 3 任务停止 4 已取消 5 执行失败
     */
    private String taskStatus;

    /**
     * 执行结果  0 失败  1 成功
     */
    private String runResult;

    /**
     * 运行开始时间
     */
    private LocalDateTime runStartTime;

    /**
     * 运行比例 失败/取消 归0  成功 100
     */
    private String runTimeRadio;

    /**
     * 运行时间 系统自动计算
     */
    private String runTime;

    /**
     * 提示信息 失败信息
     */
    private String runTips;

    /**
     * 排产类型 1全局排产，2排产
     */
    private String runType;

    /**
     * 当前节点
     */
    private String currentNode;

    /**
     * 当前指令
     */
    private String currentInstruct;

    /**
     * 算法类型 1.MK 2.Sewing逆拉 3.Sewing前工段排程 4.Sewing正推 5.全工段排程 0 全局排产
     */
    private String optimizeType;
}