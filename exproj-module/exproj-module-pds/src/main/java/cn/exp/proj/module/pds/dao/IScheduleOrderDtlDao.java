package cn.exp.proj.module.pds.dao;

import cn.exp.proj.module.pds.entity.ScheduleOrderDtlEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: tanchao
 * @date: 2023-05-16  14:01
 * @description: 排程明细
 * @version: 1.0
 */
@Mapper
public interface IScheduleOrderDtlDao extends BaseMapper<ScheduleOrderDtlEntity> {
}

