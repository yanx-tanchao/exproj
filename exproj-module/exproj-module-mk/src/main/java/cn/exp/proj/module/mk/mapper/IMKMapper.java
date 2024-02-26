package cn.exp.proj.module.mk.mapper;

import cn.exp.proj.module.mk.vo.BatchKitVo;
import cn.exp.proj.module.mk.vo.MKOrderVo;
import cn.exp.proj.module.mk.vo.MKOrderDtlVo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IMKMapper {
    public static IMKMapper INSTANCE = Mappers.getMapper(IMKMapper.class);

    MKOrderVo toMKOrder(MKOrderDtlVo order);

    BatchKitVo newArrivePlan(BatchKitVo arrivePlan);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "materialCode", target = "materialCode")
    @Mapping(source = "mkOrderId", target = "mkOrderId")
    @Mapping(source = "mkOrderTaskId", target = "mkOrderTaskId")
    @Mapping(source = "tenantId", target = "tenantId")
    @Mapping(source = "siteId", target = "siteId")
    @Mapping(source = "siteCode", target = "siteCode")
    @Mapping(source = "siteName", target = "siteName")
    @Mapping(source = "reported", target = "reported")
    @Mapping(source = "needHandle", target = "needHandle")
    BatchKitVo newMRKitPlan(BatchKitVo arrivePlan);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "mkOrderId", target = "mkOrderId")
    @Mapping(source = "mkTaskId", target = "mkOrderTaskId")
    @Mapping(source = "materialCode", target = "materialCode")
    @Mapping(source = "tenantId", target = "tenantId")
    @Mapping(source = "siteId", target = "siteId")
    @Mapping(source = "siteCode", target = "siteCode")
    @Mapping(source = "siteName", target = "siteName")
    @Mapping(target = "reported", constant = "1")
    @Mapping(target = "needHandle", constant = "1")
    BatchKitVo toArrivePlan(MKOrderDtlVo dtlVo);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "mkOrderId", target = "mkOrderId")
    @Mapping(source = "materialCode", target = "materialCode")
    @Mapping(source = "tenantId", target = "tenantId")
    @Mapping(source = "siteId", target = "siteId")
    @Mapping(source = "siteCode", target = "siteCode")
    @Mapping(target = "reported", constant = "1")
    @Mapping(target = "needHandle", constant = "1")
    BatchKitVo toArrivePlan(MKOrderVo mkOrder);
}
