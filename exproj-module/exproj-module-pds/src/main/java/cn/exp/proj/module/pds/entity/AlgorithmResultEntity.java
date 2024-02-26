package cn.exp.proj.module.pds.entity;

import cn.exp.proj.common.core.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 外部算法计算结果表
 * </p>
 *
 * 
 * @date: 2023-05-23
 */
@Data
public class AlgorithmResultEntity extends BaseEntity<AlgorithmResultEntity> {
    /**
     * 批次消息号
     */
    private Long messageId;

    /**
     * 版本
     */
    private String version;

    /**
     * 场景
     */
    private Long scenarioId;

    /**
     * 合并Id
     */
    private Long originMkOrderId;

    /**
     * 原Task - 拆分前
     */
    private String originMkTask;

    /**
     * task排程顺序
     */
    private Long taskSequence;

    /**
     * 单位id
     */
    private String taskId;

    /**
     * 需求数量/净需求
     */
    private BigDecimal qty;

    /**
     * 已完成的量
     */
    private BigDecimal completedQty;

    /**
     * 单位id
     */
    private String unit;

    /**
     * 生产开始时间
     */
    private LocalDateTime startDateTime;

    /**
     * 生产结束
     */
    private LocalDateTime endDateTime;

    /**
     * 生产时长
     */
    private BigDecimal persistentTime;

    /**
     * 标准工时
     */
    private BigDecimal standardTime;

    /**
     * 资源id
     */
    private Long resourceId;

    /**
     * 资源编码
     */
    private String resourceCode;

    /**
     * 任务组
     */
    private String taskGroup;

    /**
     * 排产订单状态
     */
    private String planStatus;

    //~~~~~~~~~~~~ 2023-09-25 新增  begin ~~~~~~~~~~~~~~~~
    /**
     * 工段
     */
    private String prdProcess;
    /**
     * 分片数量
     */
    private String taskPart;

    //~~~~~~~~~~~~ 2023-09-25 新增  end   ~~~~~~~~~~~~~~~~

    //~~~~~~~~~~~~ 2023-10-07 新增  begin ~~~~~~~~~~~~~~~~
    /**
     * 锁定状态: 0 未锁定，1锁定资源+时间， 2锁定时间，3锁定资源（郑俊柯，勿删）
     */
    private String lockStatus;

    /**
     * 幅位/部件
     */
    private String partCode;

    /**
     * 部位
     */
    private String componentCode;

    /**
     * 花型
     */
    private String patternNo;

    /**
     * 套装标志
     */
    private String garmentComponent;

    /**
     * 颜色
     */
    private String color;

    /**
     * 尺码
     */
    private String size;

    /**
     * printing转网时间，计算KPI用
     */
    private String changeBoardTime;

    //~~~~~~~~~~~~ 2023-10-07 新增  end ~~~~~~~~~~~~~~~~

}
