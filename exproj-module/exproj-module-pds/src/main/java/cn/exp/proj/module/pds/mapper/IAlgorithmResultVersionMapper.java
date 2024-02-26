package cn.exp.proj.module.pds.mapper;

import cn.exp.proj.common.core.converter.IMapStruct;
import cn.exp.proj.module.pds.api.dto.PlanOrderInfoDto;
import cn.exp.proj.module.pds.api.dto.TargetDto;
import cn.exp.proj.module.pds.entity.AlgorithmResultVersionEntity;
import cn.exp.proj.module.pds.vo.AlgorithmResultVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IAlgorithmResultVersionMapper extends IMapStruct<AlgorithmResultVersionEntity, AlgorithmResultVo> {
    @Mapping(target = "preferenceCost", source = "targetDto.apPreferenceCost")
    @Mapping(target = "kittingTargetCost", source = "targetDto.apKittingTargetCost")
    @Mapping(target = "comprehensiveValue", source = "targetDto.apComprehensiveValue")
    AlgorithmResultVersionEntity toEntity(PlanOrderInfoDto planOrderInfoDto, TargetDto targetDto);
}
