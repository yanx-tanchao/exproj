package cn.exp.proj.module.mdm.mapper;

import cn.exp.proj.common.core.converter.IMapStruct;
import cn.exp.proj.module.mdm.entity.ResourceEntity;
import cn.exp.proj.module.mdm.vo.ResourceVo;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IResourceMapper extends IMapStruct<ResourceEntity, ResourceVo> {
}
