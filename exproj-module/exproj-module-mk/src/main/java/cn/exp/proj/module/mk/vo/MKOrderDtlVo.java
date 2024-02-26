package cn.exp.proj.module.mk.vo;


import cn.exp.proj.common.core.BaseVo;
import cn.exp.proj.common.core.util.StringUtil;
import cn.exp.proj.common.core.wrapper.BooleanWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class MKOrderDtlVo extends BaseVo {
    /**
     * 合并订单表的id
     */
    private Long mkOrderId;

    /**
     * 订单类型1:projection；2:Forecast；3销售订单；4外接订单；
     */
    private String orderType;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 单位Code
     */
    private String unitCode;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 计划数量
     */
    private BigDecimal planQty;

    /**
     * 净需求数量/净需求
     */
    private BigDecimal qty;

    /**
     * 计划单号
     */
    protected String planOrder;

    /**
     * 联动关联编号 根据PO+颜色拼接生成
     */
    private String collaborationCode;

    /**
     * 上层计划单号
     * 部件级别
     */
    protected String upperMkOrder;

    /**
     * 后层计划单号
     * 工段级别
     */
    private String upperMkOrderPrdProcess;

    /**
     * 后层计划单号
     * 工段级别
     */
    private String afterPrdProcess;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户交货期
     */
    private LocalDateTime deliveryDate;

    /**
     * 客户交货期
     */
    private LocalDateTime crsd;

    /**
     * 生产交货期
     */
    private LocalDateTime planFinishDate;

    /**
     * 计划下达日期
     */
    private LocalDateTime planReleaseDate;

    /**
     * 订单类型（so、projction）
     */
    private String orderCategory;

    /**
     * 订单优先级
     */
    private String priorityCode;

    /**
     * 计划锁定
     */
    private String holdFlag;

    /**
     * 销售订单
     */
    private String so;

    private String projectionNo;

    private Long projectionItemNo;

    /**
     * 层级
     */
    private Integer level;

    /**
     * bomId
     */
    private Long bomId;

    /**
     * 制成段
     */
    private String prdProcess;

    /**
     * bom item id
     */
    private Long bomItemId;

    /**
     * 场景id 原来是版本号
     */
    private Long scenarioId;

    /**
     * 销售订单行号
     */
    private Long soItem;

    /**
     * 销售订单子行号
     */
    private Long subItem;

    /**
     * hg_mdm_material_bom.bom_version，bom的最大版本
     */
    private String bomVersion;

    /**
     * 颜色
     */
    private String color;

    /**
     * 尺码
     */
    private String size;

    /**
     * 客户po
     */
    private String customerPo;

    /**
     * 工厂款号
     */
    private String factoryStyleNo;

    /**
     * 客户款号
     */
    private String customerStyleNo;

    /**
     * 发货国家
     */
    private String shipCountry;

    /**
     * 发货地区
     */
    private String shipArea;

    /**
     * 客户编码
     */
    private String customerCode;

    private String custNameShort;

    /**
     * 系列
     */
    private String series;

    /**
     * 工艺
     */
    private String specialProcess;

    /**
     * 产品大类
     */
    private String masterCategory;

    /**
     * 产品小类
     */
    private String subCategory;

    /**
     * 特殊工艺
     */
    private String specialProces;

    /**
     * 订单的优先级类型 qr , normal
     */
    private String priorityType;

    /**
     * APS ID
     */
    private String apsId;

    /**
     * 物料类别
     */
    private String materialCategory;

    /**
     * 上层计划订单id
     */
    private Long parentPlanOrderId;

    /**
     * 物料分配运算配置Id
     */
    private Long ruleConfigId;

    /**
     * 客户部门
     */
    private String customerDivision;

    /**
     * 服装部位（套装）
     */
    private String garmentComponent;

    /**
     * 套装编号
     */
    private String suitNo;

    /**
     * 外协标识
     */
    private String orderOutsource;

    /**
     * 齐套数量
     */
    private BigDecimal kittingQty;

    /**
     * 幅位
     */
    private String part;

    /**
     * 部件
     */
    private String component;

    /**
     * 部位
     */
    private String position;

    /**
     * 部件分组id
     * 工段下所有的部件 部位 工段级别计划订单共用一个id
     */
    private Long partGroupId;

    /**
     * 关键物料开始时间
     */
    private LocalDateTime fbBodyStartDate;

    /**
     * 关键布料结束时间
     */
    private LocalDateTime fbBodyEndDate;

    /**
     * 任务编号
     */
    private String taskNo;

    private Long taskId;

    private Long mkTaskId;

    @ApiModelProperty("任务")
    private String task;

    @ApiModelProperty("排程任务表主键id")
    private Long scheduleOrderId;

    @ApiModelProperty("锁定状态	0 未锁，	1 锁定资源+时间，	2 锁定时间，	3 锁定资源")
    private String lockStatus;

    private BigDecimal prdProcessSeq;


    @ApiModelProperty("部位")
    private String componentCode;

    @ApiModelProperty("幅位")
    private String partCode;

    @ApiModelProperty("1 工段  0 部件  部位")
    private Integer prdType;

    @ApiModelProperty("1 不参与计算 ")
    private Integer notCost;

    private String itemNo;

    private String nextItemNo;

    private String processCode;

    private Integer sort;

    private BigDecimal soQty;

    @ApiModelProperty("季节款")
    private String season;

    @ApiModelProperty("套装数量")
    private Integer componentPack;

    @ApiModelProperty("外发标识  0是不外发，1是外发")
    private Integer subcontractFlag;

    @ApiModelProperty("提前期数量")
    private BigDecimal finishLeadTimeNum = BigDecimal.ZERO;

    @ApiModelProperty("花型")
    private String patternNo;

    @ApiModelProperty("排程开始时间")
    private LocalDateTime startDateTime;

    @ApiModelProperty("排除kniting  1排除")
    private Integer knitingExclude = 0;

    @ApiModelProperty("幅位个数")
    private Integer partCodeNum;

    @ApiModelProperty("（订单标识：1.外接，0.本工厂生产）")
    private String orderFlag;

    @Override
    public String toString() {
        return StringUtil.toSnakeJson(this);
    }

    public String getMatchedKey() {
        StringBuilder builder = new StringBuilder();
        builder.append(so);
        builder.append(soItem);
        builder.append(projectionNo);
        builder.append(projectionItemNo);
        builder.append(prdProcess);
        builder.append(prdProcessSeq);
        builder.append(level);
        builder.append(prdType);
        builder.append(componentCode);
        builder.append(partCode);

        addMRCond(builder);
        return builder.toString();
    }

    /**
     * 匹配是否可以合并订单
     *
     * @param otherEntity
     * @return
     */
    public boolean mergable(MKOrderDtlVo otherEntity) {
        if (Objects.isNull(otherEntity)) {
            return Boolean.FALSE;
        }

        BooleanWrapper wrapper = BooleanWrapper.empty();
        wrapper.and(Objects.equals(prdProcess, otherEntity.getPrdProcess()))
               .and(Objects.equals(prdProcessSeq, otherEntity.getPrdProcessSeq()))
               .and(Objects.equals(color, otherEntity.getColor()))
               .and(Objects.equals(level, otherEntity.getLevel()))
               .and(Objects.equals(prdType, otherEntity.getPrdType()))
               .and(Objects.equals(patternNo, otherEntity.getPatternNo()))
               .and(Objects.equals(garmentComponent, otherEntity.getGarmentComponent()));
        return wrapper.eval();
    }


    /**
     * 生成物料号
     *
     * @return
     */
    public String buildMaterial() {
        if (Objects.isNull(materialCode)) {
            return factoryStyleNo + "_" + color + "_" + size;
        }
        return materialCode;
    }

    public String buildMRKey() {
        StringBuilder builder = new StringBuilder();
        builder.append(projectionNo);
        builder.append(projectionItemNo);
        addMRCond(builder);
        return builder.toString();
    }

    public String buildSoMRKey() {
        StringBuilder builder = new StringBuilder();
        builder.append(so);
        builder.append(soItem);
        addMRCond(builder);
        return builder.toString();
    }

    /**
     * 报工分组
     *
     * @return
     */
    public String buildReportedKey() {
        StringBuilder builder = new StringBuilder();
        builder.append(color);
        builder.append(patternNo);
        return builder.toString();
    }

    /**
     * 添加订单合并基础条件(颜色,花型,部位)
     *
     * @param builder
     */
    private void addMRCond(StringBuilder builder) {
        builder.append(color);
        builder.append(patternNo);
        builder.append(garmentComponent);
    }
}
