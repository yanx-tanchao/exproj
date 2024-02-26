package cn.exp.proj.module.pds.service.impl;

import cn.exp.proj.module.pds.dao.IAlgorithmResultVersionDao;
import cn.exp.proj.module.pds.entity.AlgorithmResultVersionEntity;
import cn.exp.proj.module.pds.service.IAlgorithmResultVersionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AlgorithmResultVersionServiceImpl
        extends ServiceImpl<IAlgorithmResultVersionDao, AlgorithmResultVersionEntity>
        implements IAlgorithmResultVersionService {
}
