package cn.exp.proj.module.pds.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AlgorithmResultVo implements ISchduleTask {
    private Long id;

    /**
     * 批次消息号
     */
    private Long messageId;

    /**
     * 版本
     */
    private String version;

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
     * 合并Id
     */
    private String mkOrder;

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
    private Integer taskStartDay;

    private Long taskStartTime;

    private Integer taskEndDay;

    private Long taskEndTime;

    /**
     * 生产时长
     */
    private Integer persistentTime;

    /**
     * 转网时间
     */
    private Integer changeNetTime;

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

    private List<AlgorithmTimeVo> algorithmTimes;

    /**
     * 生产交货期
     */
    private LocalDateTime planFinishDate;

    /**
     * 制成段
     */
    private String prdProcess;

    /**
     * 本厂款号
     */
    private String factoryStyleNo;

    /**
     * 客户款号
     */
    private String customerStyleNo;

    /**
     * 联动关联编号 根据PO+颜色拼接生成  用来区分同一张订单
     */
    private String collaborationCode;

    /**
     * 任务状态
     * 0 已计划  算法排的数据
     * （未锁定）
     * 1 锁定资源+时间
     * 2 锁定时间
     * 3 锁定资源
     * 4 未计划  算法没有排的数据
     * 5 已下达  下达功能操作后的数据
     */
    private String planStatus;

    private Integer learningCurveDay;

    private BigDecimal learningEfficiency;

    /**
     * 分片数量
     */
    private String taskPart;

    /**
     * 锁定状态: 0 未锁定，1锁定资源+时间， 2锁定时间，3锁定资源（郑俊柯，勿删）
     */
    private int lockStatus;

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
    private BigDecimal changeBoardTime;

    @Override
    public Integer getStartDay() {
        return taskStartDay;
    }

    @Override
    public Long getStartTime() {
        return taskStartTime;
    }

    @Override
    public Integer getEndDay() {
        return taskEndDay;
    }

    @Override
    public Long getEndTime() {
        return taskEndTime;
    }
}
