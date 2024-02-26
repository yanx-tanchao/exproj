package cn.exp.proj.module.mk.vo;

import cn.exp.proj.common.core.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 分批齐套的信息承载
 *
 * @date: 2023-05-16  18:43
 * @version: 1.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class BatchKitVo extends BaseEntity<BatchKitVo> {
    public Long mkOrderId;

    public Long mkOrderTaskId;

    public Long scenarioId;

    // 物料编码
    public String materialCode;

    // 齐套数量
    public BigDecimal kittingQty;

    // 能满足后工序的剩余齐套数量
    public BigDecimal qty;

    // 齐套类型(报工0/非报工1)
    public Integer reported;

    // 是否需要增加加工时时间,即后工段最早开始时间是否需要加上提前期
    @TableField(exist = false)
    public Integer needHandle;

    // 齐套时间
    public LocalDateTime kittingDate;

    // 计划最早开始时间
    public LocalDateTime earlistStartTime;

    public LocalDateTime planStartTime;

    private String siteName;
}
