package cn.exp.proj.module.pds.service.impl;

import cn.exp.proj.module.pds.dao.IAlgorithmResultDao;
import cn.exp.proj.module.pds.entity.AlgorithmResultEntity;
import cn.exp.proj.module.pds.service.IAlgorithmResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AlgorithmResultServiceImpl extends ServiceImpl<IAlgorithmResultDao, AlgorithmResultEntity>
        implements IAlgorithmResultService {
}
