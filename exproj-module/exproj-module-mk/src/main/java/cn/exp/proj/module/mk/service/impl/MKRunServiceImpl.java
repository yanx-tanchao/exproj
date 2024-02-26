package cn.exp.proj.module.mk.service.impl;

import cn.exp.proj.module.mk.component.MKCombiner;
import cn.exp.proj.module.mk.component.MKKitHandler;
import cn.exp.proj.module.mk.service.IMKRunService;
import cn.exp.proj.module.mk.vo.MKOrderDtlVo;
import cn.exp.proj.module.mk.vo.MKOrderVo;
import cn.exp.proj.module.mk.vo.MKRunParamVo;
import cn.exp.proj.module.mk.vo.MaterialKitVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MKRunServiceImpl implements IMKRunService {
    @Override
    public void distribute(MKRunParamVo mkRunParam, List<MKOrderDtlVo> orderDtls) {
        MaterialKitVo kitVo = new MaterialKitVo();
        List<MKOrderVo> mkOrders = new ArrayList<>();

        MKCombiner combiner = new MKCombiner();
        combiner.init(mkRunParam, orderDtls);
        combiner.combine(orderDtls);
        kitVo.putAll(combiner.getKitPlans());
        mkOrders.addAll(combiner.getAllMKOrders());

        // 物料齐套状态运算结束
        // 根据提前期计算最早开始时间
        MKKitHandler worker = new MKKitHandler();
        worker.setKitInfo(kitVo);
        worker.init(mkRunParam, mkOrders);
        worker.handle();
    }
}
