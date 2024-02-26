package cn.exp.proj.module.mdm.service;

import cn.exp.proj.module.mdm.entity.ResourceEntity;
import cn.exp.proj.module.mdm.vo.ResourceVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IResourceService extends IService<ResourceEntity> {
    ResourceVo getById(Long id);

    List<ResourceVo> list(ResourceVo resourceVo);

    ResourceVo save(ResourceVo resourceVo);

    ResourceVo update(ResourceVo resourceVo);

    ResourceVo delete(Long id);
}
