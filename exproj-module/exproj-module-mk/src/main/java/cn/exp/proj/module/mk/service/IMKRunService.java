package cn.exp.proj.module.mk.service;

import cn.exp.proj.module.mk.vo.MKOrderDtlVo;
import cn.exp.proj.module.mk.vo.MKRunParamVo;

import java.util.List;

public interface IMKRunService {
    /**
     * 计算各工段齐套状态,并生成对应的子计划订单的信息
     *
     * @return 工段级加物料级的所有订单集合
     */
    void distribute(MKRunParamVo mkRunParam, List<MKOrderDtlVo> orderDtls);
}
