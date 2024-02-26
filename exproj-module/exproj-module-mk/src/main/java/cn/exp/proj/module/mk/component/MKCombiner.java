package cn.exp.proj.module.mk.component;

import cn.exp.proj.common.core.util.CollectionUtil;
import cn.exp.proj.common.core.util.MathUtil;
import cn.exp.proj.common.core.util.StringUtil;
import cn.exp.proj.common.core.wrapper.BooleanWrapper;
import cn.exp.proj.module.mk.component.util.MRType;
import cn.exp.proj.module.mk.constant.IMKConstant;
import cn.exp.proj.module.mk.vo.BatchKitVo;
import cn.exp.proj.module.mk.vo.MKOrderVo;
import cn.exp.proj.module.mk.mapper.IMKMapper;
import cn.exp.proj.module.mk.vo.MKOrderDtlVo;
import cn.exp.proj.module.mk.vo.MKRunParamVo;
import cn.exp.proj.module.mk.vo.MaterialKitVo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * MK合单
 */
@Slf4j
public class MKCombiner {
    // 合单条件
    private Function<MKOrderDtlVo, String> mrKeyExtractor = MKOrderDtlVo::buildSoMRKey;

    @Setter
    private Function<MKOrderDtlVo, MaterialKitVo> allocate;

    @Setter
    private Function<MKOrderDtlVo, MaterialKitVo> allocateReported;

    // 尾工段集合(按成品物料合单)
    @Getter
    private Map<String, List<MKOrderDtlVo>> finalStageMap;

    // 合单MK_Order
    @Getter
    private List<MKOrderVo> allMKOrders;

    // 合单后齐套记录, 按task合并, 按order保存
    @Getter
    private Map<Long, List<BatchKitVo>> kitPlans;

    public void init(MKRunParamVo mkRunParam, List<MKOrderDtlVo> mkOrderDtls) {
        this.allMKOrders = new ArrayList<>();
        this.kitPlans = new HashMap<>();

        // 找出本次计算的所有成品, 按照优先级进行排序
        this.finalStageMap = buildRoute(mkOrderDtls, mkOrderDtls);
    }

    /**
     * 为所有的成品构建工艺路线
     *
     * @param finishedOrders 成品计划订单
     * @param orders         所有的计划订单
     */
    private Map<String, List<MKOrderDtlVo>> buildRoute(List<MKOrderDtlVo> finishedOrders, List<MKOrderDtlVo> orders) {
        Map<String, List<MKOrderDtlVo>> result = new LinkedHashMap<>();
        Map<String, List<MKOrderDtlVo>> allOrdersMap = CollectionUtil.group(orders, MKOrderDtlVo::getApsId);
        for (MKOrderDtlVo finishdOrder : finishedOrders) {
            List<MKOrderDtlVo> list = new ArrayList<>();
            list.add(finishdOrder);
            List<MKOrderDtlVo> apsPlanOrderList = allOrdersMap.get(finishdOrder.getApsId());
            Map<String, MKOrderDtlVo> planOrderMap = CollectionUtil.toMap(apsPlanOrderList, MKOrderDtlVo::getAfterPrdProcess);

            // 接力订单
            MKOrderDtlVo currentOrder = finishdOrder;
            while (currentOrder != null) {
                currentOrder = planOrderMap.get(currentOrder.getPlanOrder());
                if (Objects.nonNull(currentOrder)) {
                    list.add(currentOrder);
                }
            }
            result.put(finishdOrder.getPlanOrder(), list);
        }
        return result;
    }

    public void combine(List<MKOrderDtlVo> mkOrderDtls) {
        // 把所有的成品计划订单根据so + po + color + garmentComponent + patternNo 进行分组
        Map<String, List<MKOrderDtlVo>> mrKeyMap = CollectionUtil.linkGroup(mkOrderDtls, mrKeyExtractor);
        mrKeyMap.forEach((k, v) -> doCombine(CollectionUtil.first(v), order -> mrQuery(v, order)));
    }

    private List<MKOrderDtlVo> mrQuery(List<MKOrderDtlVo> finishedOrders, MKOrderDtlVo orderDtl) {
        List<MKOrderDtlVo> result = new ArrayList<>();
        for (MKOrderDtlVo finishedOrder : finishedOrders) {
            List<MKOrderDtlVo> orderDtlVos = finalStageMap.get(finishedOrder.getPlanOrder());
            List<MKOrderDtlVo> mrDtlVos = CollectionUtil.filter(orderDtlVos, orderDtl::mergable);
            result.addAll(mrDtlVos);
        }
        return result;
    }

    /**
     * 分组, 合单, 物料分配
     *
     * @param mkOrderDtl 运行订单
     * @param querier    可合并订单查询器
     */
    public void doCombine(MKOrderDtlVo mkOrderDtl, Function<MKOrderDtlVo, List<MKOrderDtlVo>> querier) {
        // 创建一个缓存,存储已经计算过的工段
        Map<String, Boolean> dtlCache = new HashMap<>();

        // 拉出改成品订单的工艺路线
        List<MKOrderDtlVo> routingLink = finalStageMap.get(mkOrderDtl.getPlanOrder());
        for (MKOrderDtlVo routeOrder : routingLink) {
            // 判断缓存中是否已经存在订单号, 如果存在订单号则不参与计算, 如果工段名相同则创建合单数据, 复制之前的运算结果保存
            String prdProcess = routeOrder.getPrdProcess();
            String orderNo = routeOrder.getPlanOrder();
            Boolean needCal = Objects.isNull(dtlCache.get(prdProcess));

            // 找出可以合并的订单
            List<MKOrderDtlVo> orderCanMerge = querier.apply(routeOrder);

            // 保存合并订单数据和计划订单的关联
            orderCanMerge.remove(routeOrder);
            orderCanMerge.add(0, routeOrder);
            List<MKOrderVo> mkOrderList = doCombine(routeOrder, orderCanMerge);

            allMKOrders.addAll(mkOrderList);

            if (!needCal) {
                // 重复工段, 复制分批齐套
                mkOrderList.forEach(dtl -> doCopyKit(dtl, routeOrder));
                continue;
            }

            doAllocate(orderCanMerge, mkOrderList);

            // 放入缓存
            dtlCache.put(prdProcess, Boolean.TRUE);
        }
    }

    private void doCopyKit(MKOrderVo mkOrder, MKOrderDtlVo routeOrder) {
        // 找出之前计算过的合并订单
        List<MKOrderVo> calculatedMergeOrder = CollectionUtil.filter(allMKOrders, item -> {
            BooleanWrapper wrapper = BooleanWrapper.of(StringUtil.eq(item.getPrdProcess(), mkOrder.getPrdProcess()));
            return wrapper.eval();
        });

        // 找出之前计算过的分批齐套信息
        MKOrderVo orderDtl = CollectionUtil.first(calculatedMergeOrder);

        // 复制之前的计算结果 - 优先处理报工
        List<BatchKitVo> kitPlans = this.kitPlans.get(orderDtl.getId());
        List<BatchKitVo> copyKitPlans = CollectionUtil.map(kitPlans, IMKMapper.INSTANCE::newArrivePlan);
        BigDecimal reportedQty = MathUtil.subtract(mkOrder.getPlanQty(), mkOrder.getQty());
        List<BatchKitVo> copyKits = new ArrayList<>();
        if (MathUtil.gtZero(reportedQty)) {
            BatchKitVo reportedKit = IMKMapper.INSTANCE.toArrivePlan(routeOrder);
            reportedKit.setReported(IMKConstant.KIT_TYPE_REPORTED);
            reportedKit.setMkOrderId(mkOrder.getId());
            reportedKit.setKittingQty(reportedQty);
            copyKits.add(reportedKit);
        }
        for (BatchKitVo kitPlan : copyKitPlans) {
            if (MathUtil.leZero(reportedQty)) {
                break;
            }
            BigDecimal kittingQty = kitPlan.getKittingQty();
            reportedQty = MathUtil.subtract(kittingQty, reportedQty);
            if (MathUtil.geZero(reportedQty)) {
                kitPlan.setKittingQty(reportedQty);
                break;
            }
            if (MathUtil.ltZero(reportedQty)) {
                kitPlan.setKittingQty(BigDecimal.ZERO);
                reportedQty = reportedQty.abs();
            }
        }
        copyKitPlans.removeIf(kit -> MathUtil.isZero(kit.getKittingQty()));
        for (BatchKitVo kitPlan : copyKitPlans) {
            BatchKitVo copyKit = IMKMapper.INSTANCE.newArrivePlan(kitPlan);
            copyKit.setKittingQty(copyKit.getKittingQty());
            copyKit.setMkOrderId(mkOrder.getId());
            copyKits.add(copyKit);
        }
        this.kitPlans.put(mkOrder.getId(), copyKits);
    }

    private void doAllocate(List<MKOrderDtlVo> orderDtls, List<MKOrderVo> mkOrders) {
        Map<Long, List<BatchKitVo>> kitStates = dealKitBatch(orderDtls, mkOrders);
        for (MKOrderVo mkOrder : mkOrders) {
            // 分批齐套合并, 如果是KNITTING 不合并
            Long mapKey = mkOrder.getId();
            List<BatchKitVo> kitPlans = kitStates.get(mapKey);
            kitPlans = MRType.BY_REPORTED.merge(kitPlans);
            kitPlans.forEach(kit -> kit.setMkOrderId(mkOrder.getId()));

            this.kitPlans.put(mkOrder.getId(), kitPlans);
        }
    }

    private Map<Long, List<BatchKitVo>> dealKitBatch(List<MKOrderDtlVo> orderDtls, List<MKOrderVo> mkOrderList) {
        List<MaterialKitVo> reportedResults = CollectionUtil.map(orderDtls, allocateReported);
        List<MaterialKitVo> results = CollectionUtil.map(orderDtls, allocate);
        MKOrderVo mkOrderVo = CollectionUtil.first(mkOrderList);
        Map<Long, List<BatchKitVo>> kitStates = CollectionUtil.newHashMap();

        // 报工齐套结果集
        for (MaterialKitVo reportedResult : reportedResults) {
            Map<Long, List<BatchKitVo>> kittingState = reportedResult.getKitMap();
            kittingState.forEach((k, v) -> kitStates.compute(k, (k1, v1) -> CollectionUtil.orAddAll(v1, v)));
        }

        // 分批齐套结果集
        for (MaterialKitVo result : results) {
            Map<Long, List<BatchKitVo>> kittingState = result.getKitMap();
            kittingState.forEach((k, v) -> kitStates.compute(k, (k1, v1) -> CollectionUtil.orAddAll(v1, v)));
        }

        Map<Long, List<BatchKitVo>> newKitStates = CollectionUtil.newHashMap();
        newKitStates.put(mkOrderVo.getId(), CollectionUtil.flatMap(kitStates));
        return newKitStates;
    }

    /**
     * 创建合单数据, 创建合单实体类
     *
     * @param orderDtlVo  运行订单
     * @param mkOrderDtls 可合并订单
     * @return
     */
    private List<MKOrderVo> doCombine(MKOrderDtlVo orderDtlVo, List<MKOrderDtlVo> mkOrderDtls) {
        MKOrderVo mkOrder = IMKMapper.INSTANCE.toMKOrder(orderDtlVo);
        mkOrder.setMkOrder(orderDtlVo.getPlanOrder());
        mkOrder.setPlanQty(MathUtil.sum(mkOrderDtls, MKOrderDtlVo::getPlanQty));
        mkOrder.setQty(MathUtil.sum(mkOrderDtls, MKOrderDtlVo::getQty));
        for (MKOrderDtlVo om : mkOrderDtls) {
            om.setMkOrderId(mkOrder.getId());
        }
        return Arrays.asList(mkOrder);
    }

}
