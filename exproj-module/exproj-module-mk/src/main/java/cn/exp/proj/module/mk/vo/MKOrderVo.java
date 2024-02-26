package cn.exp.proj.module.mk.vo;

import cn.exp.proj.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @date: 2023-05-19  11:16
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MKOrderVo extends BaseEntity<MKOrderVo> {
    private Long scenarioId;

    private String mkOrder;

    private String collaborationCode;

    protected String upperMkOrder;

    protected String originUpperMkOrder;

    private String prdProcess;

    private BigDecimal prdProcessSeq;

    private String color;

    private String patternNo;

    private String size;

    private String factoryStyleNo;

    private String customerStyleNo;

    private String partCode;

    private String componentCode;

    private Long partGroupId;

    private BigDecimal planQty;

    private BigDecimal qty;

    private String unitCode;

    private LocalDateTime deliveryDate;

    private LocalDateTime planFinishDate;

    private LocalDateTime planReleaseDate;


    private BigDecimal kittingQty;

    private String priorityCode;

    private String holdFlag;

    private Integer level;

    private Long bomId;

    private String bomVersion;

    private Long bomItemId;

    private String garmentComponent;

    private String suitNo;

    private String orderNo;

    private Long orderItemNo;

    private String priorityType;

    private String orderType;

    private String customerCode;

    private String customerName;

    private String customerPo;

    private String shipCountry;

    private String shipArea;

    private String masterCategory;

    private String subCategory;

    private String series;

    private String specialProcess;

    private String upperMkOrderPrdProcess;

    private Integer orderStatus;

    public String getMatchedKey() {
        return getComponentCode()
                + getPartCode()
                + getPrdProcess()
                + getPrdProcessSeq()
                + getOrderNo()
                + getCustomerPo()
                + getColor()
                + getLevel()
                + getGarmentComponent();
    }

    public String mrKey() {
        return orderNo + orderItemNo + color + patternNo + garmentComponent;
    }
}
