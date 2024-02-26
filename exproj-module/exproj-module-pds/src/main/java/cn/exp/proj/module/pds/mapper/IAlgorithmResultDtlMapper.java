package cn.exp.proj.module.pds.mapper;

import cn.exp.proj.common.core.converter.IMapStruct;
import cn.exp.proj.module.pds.entity.AlgorithmResultDtlEntity;
import cn.exp.proj.module.pds.vo.AlgorithmResultDtlVo;
import cn.exp.proj.module.pds.vo.AlgorithmTimeVo;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IAlgorithmResultDtlMapper extends IMapStruct<AlgorithmResultDtlEntity, AlgorithmResultDtlVo> {
    AlgorithmResultDtlVo toVo(AlgorithmTimeVo algorithmTimeVo);
}
