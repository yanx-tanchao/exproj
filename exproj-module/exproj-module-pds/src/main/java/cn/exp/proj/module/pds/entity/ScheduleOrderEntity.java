package cn.exp.proj.module.pds.entity;

import cn.exp.proj.common.core.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 排程任务表
 */
@Data
public class ScheduleOrderEntity extends BaseEntity<ScheduleOrderEntity> {
    /**
     * 场景id
     */
    private Long scenarioId;

    /**
     * 版本
     */
    private String version;

    /**
     * 任务id 算法返回的拆分后的taskID
     */
    private String taskId;

    /**
     * 合并表ID（原taskid） 将合并表ID作为需求的原始taskID传递给算法，算法拆分订单时会记录初始taskID,并随排产结果返回
     */
    private String originMkOrderId;

    /**
     * 原Task - 拆分前
     */
    private String originMkTask;

    /**
     * task排程顺序
     */
    private Long taskSequence;

    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 已完工数量 报工数据定时更新(不重算开始结束)/计划更新、排产、拖动（需重算开始结束）
     */
    private BigDecimal completedQty;

    /**
     * 单位id
     */
    private Long unit;

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
     * 标准工时
     */
    private BigDecimal standardTime;

    /**
     * 资源id
     */
    private Long resourceId;

    /**
     * 资源code
     */
    private String resourceCode;

    /**
     * 任务组 vap同资源开多组时，记录多组的任务拼接字符串；例：task1;task2;task3
     */
    private String taskGroup;

    /**
     * 任务状态 未计划；已计划；已锁定；已下达
     */
    private String planStatus;

    /**
     * 锁定状态 0未锁定，1锁定时间+资源 2锁定时间  3锁定资源
     */
    private String lockStatus;

    //~~~~~~~~~~~~ 2023-10-07 新增  begin ~~~~~~~~~~~~~~~~

    /**
     * 分片数量
     */
    private String taskPart;

    /**
     * 工段
     */
    private String prdProcess;

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
