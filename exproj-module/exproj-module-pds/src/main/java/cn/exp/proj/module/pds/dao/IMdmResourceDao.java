package cn.exp.proj.module.pds.dao;

import cn.exp.proj.module.pds.entity.ResourceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author: tanchao
 * @date: 2023-05-16  14:01
 * @description: 资源
 * @version: 1.0
 */
@Mapper
public interface IMdmResourceDao extends BaseMapper<ResourceEntity> {
}
