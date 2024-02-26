package cn.exp.proj.module.mdm.service.impl;

import cn.exp.proj.common.core.asserts.ProjAssert;
import cn.exp.proj.common.core.util.CollectionUtil;
import cn.exp.proj.module.mdm.dao.IResouceDao;
import cn.exp.proj.module.mdm.entity.ResourceEntity;
import cn.exp.proj.module.mdm.mapper.IResourceMapper;
import cn.exp.proj.module.mdm.service.IResourceService;
import cn.exp.proj.module.mdm.vo.ResourceVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ResourceServiceImpl extends ServiceImpl<IResouceDao, ResourceEntity> implements IResourceService {
    private IResourceMapper iResourceMapper;

    @Override
    public ResourceVo getById(Long id) {
        return iResourceMapper.toVo(baseMapper.selectById(id));
    }

    @Override
    public List<ResourceVo> list(ResourceVo resourceVo) {
        return CollectionUtil.map(list(), iResourceMapper::toVo);
    }

    @Override
    public ResourceVo save(ResourceVo resourceVo) {
        ResourceEntity resourceEntity = iResourceMapper.toEntity(resourceVo);
        save(resourceEntity);
        return iResourceMapper.toVo(resourceEntity);
    }

    @Override
    public ResourceVo update(ResourceVo resourceVo) {
        ResourceEntity resourceEntity = iResourceMapper.toEntity(resourceVo);
        updateById(resourceEntity);
        return resourceVo;
    }

    @Override
    public ResourceVo delete(Long id) {
        ResourceEntity resourceEntity = baseMapper.selectById(id);
        ProjAssert.nonNull(resourceEntity, 10000, "资源不存在");
        removeById(id);
        return iResourceMapper.toVo(resourceEntity);
    }
}
