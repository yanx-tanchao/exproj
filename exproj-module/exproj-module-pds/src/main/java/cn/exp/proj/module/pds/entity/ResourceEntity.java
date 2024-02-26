package cn.exp.proj.module.pds.entity;

import cn.exp.proj.common.core.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @date: 2022-10-20
 */
@Data
public class ResourceEntity extends BaseEntity<ResourceEntity> {
    private String resourceCode;

    private String resourceName;

    private String resourcegroupCode;

    private String resourceType;

    private String locno;

    private String postResource;

    private BigDecimal efficiency;

    private String limitedPlan;

    private String isBottleneck;

    private String limitedCapacity;

    private String isEffective;

    private String features;

    private String color;

    private String isCloseNextTask;

    private String isFlwNextRsrc;

    private String isWorkMark;

    private String isCrossAss;

    private String isEnsureSeq;

    private String lcDaysMinus;

    private String lcDaysPlus;

    private String dayBf;

    private String dayAfta;

    private String minMfgno;

    private String maxMfgno;

    private String maxIntTime;

    private String fIntTime;

    private String bIntTime;

    private String fMfgIntTime;

    private String bMfgIntTime;

    private String splitIntTime;

    private String maxStorage;

    private String minStorage;

    private String fBuffer;

    private String bBuffer;

    private String fSetting;

    private String bSetting;

    private String allowMPro;

    private String bucketsExp;

    private String fBucketsExp;

    private String bBucketsExp;

    private String distExp;

    private String rsrcLockTime;

    private String splitExp;

    private String mfgExp;

    private String maxOverlap;

    private Integer version;

    private String resourcegroupName;

    private String schedulingModel;

    private String prdProcess;

    private String resourceTypeDesc;

    private String inputHc;
}
