package cn.exp.proj.module.pds.service.impl;


import cn.exp.proj.module.pds.dao.IScheduleOrderDao;
import cn.exp.proj.module.pds.entity.ScheduleOrderEntity;
import cn.exp.proj.module.pds.service.IScheduleOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @date: 2023-05-25 14:17:43
 */
@Service
public class ScheduleOrderServiceImpl extends ServiceImpl<IScheduleOrderDao, ScheduleOrderEntity>
        implements IScheduleOrderService {
}
