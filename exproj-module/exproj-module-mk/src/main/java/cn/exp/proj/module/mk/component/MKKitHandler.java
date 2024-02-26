package cn.exp.proj.module.mk.component;

import cn.exp.proj.common.core.asserts.ProjAssert;
import cn.exp.proj.common.core.util.CollectionUtil;
import cn.exp.proj.common.core.util.MathUtil;
import cn.exp.proj.common.core.util.MinimaxUtil;
import cn.exp.proj.common.core.util.StringUtil;
import cn.exp.proj.module.mk.component.util.MRType;
import cn.exp.proj.module.mk.constant.IMKConstant;
import cn.exp.proj.module.mk.mapper.IMKMapper;
import cn.exp.proj.module.mk.vo.BatchKitVo;
import cn.exp.proj.module.mk.vo.MKOrderVo;
import cn.exp.proj.module.mk.vo.MKRunParamVo;
import cn.exp.proj.module.mk.vo.MaterialKitVo;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 齐套计算
 * 继承前工段分批齐套
 * 并计算最早开始时间
 * 按提前期推工作时间
 */
@Slf4j
@Accessors(chain = true)
public class MKKitHandler {
    public interface LTFunction {
        BigDecimal findLTTime(List<MKOrderVo> orders, MKOrderVo order, Boolean header);
    }

    // MK任务
    protected List<MKOrderVo> mkOrders;

    // MK任务
    protected Map<String, List<MKOrderVo>> afterMap;

    // 齐套信息
    @Setter
    protected MaterialKitVo kitInfo;

    protected MKWorkCalendar mkCalendar;

    @Setter
    protected LTFunction ltFunction;

    protected Map<Long, Boolean> mkOrderKit;

    protected Map<Long, BigDecimal> mkOrderKitQty;

    public void init(MKRunParamVo planOrderVo, List<MKOrderVo> mkOrders) {
        ProjAssert.nonNull(planOrderVo);
        this.mkCalendar = planOrderVo.getMkWorkCalendar();
        this.afterMap = CollectionUtil.newHashMap();
        this.mkOrderKit = CollectionUtil.newHashMap();
        this.mkOrderKitQty = CollectionUtil.newHashMap();
        this.mkOrders = CollectionUtil.map(mkOrders);
        for (MKOrderVo mkOrder : this.mkOrders) {
            String upperMkOrder = mkOrder.getUpperMkOrder();
            if (StringUtil.isBlank(upperMkOrder)) {
                continue;
            }
            List<String> uppers = Arrays.asList(upperMkOrder.split(StringUtil.COMMA));
            uppers.forEach(upper -> this.afterMap.compute(upper, (k, v) -> CollectionUtil.orAdd(v, mkOrder)));
        }
    }

    public void handle() {
        mkOrders.forEach(this::doFollow);
    }

    /**
     * 计算当前订单预计齐套时间
     */
    private void doFollow(MKOrderVo mkOrder) {
        // 找出本工段所有的前工段
        List<MKOrderVo> uppers = CollectionUtil.reversed(afterMap.get(mkOrder.getMatchedKey()), MKOrderVo::getPrdProcessSeq);
        Long mkOrderId = mkOrder.getId();

        // 获取分批齐套的信息
        List<BatchKitVo> batchKits = CollectionUtil.orElse(kitInfo.get(mkOrderId));

        // 如果该工段没有前工段, 则该工段是首工段,
        // 计划最早开始时间 = 齐套时间 + 提前期, 实际最早开始时间 = 齐套时间 + 提前期
        if (CollectionUtil.isEmpty(uppers) && CollectionUtil.isNotEmpty(batchKits)) {
            BigDecimal processLeadTime = ltFunction.findLTTime(mkOrders, mkOrder, Boolean.TRUE);
            batchKits.forEach(e -> {
                Boolean needHandle = IMKConstant.WHETHER_YES == e.getNeedHandle() && IMKConstant.KIT_TYPE_STOCK == e.getReported();
                BigDecimal leadTime = needHandle ? processLeadTime : BigDecimal.ZERO;
                e.setPlanStartTime(mkCalendar.nextWorkDay(e.getKittingDate(), leadTime));
            });
            batchKits.forEach(e -> e.setQty(e.getKittingQty()));
            batchKits.forEach(e -> e.setEarlistStartTime(e.getPlanStartTime()));
            kitInfo.put(mkOrderId, MRType.BY_START_DATE_REPORTED.merge(batchKits));
            return;
        }

        List<BatchKitVo> beforeKits = getBeforeKits(mkOrder, uppers);
        List<BatchKitVo> newBatchKits = followBefore(mkOrder, beforeKits, batchKits);
        if (CollectionUtil.isEmpty(newBatchKits)) {
            return;
        }

        // 获取提前期
        BigDecimal processLeadTime = ltFunction.findLTTime(CollectionUtil.limit(uppers, 1), mkOrder, Boolean.FALSE);
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < newBatchKits.size(); i++) {
            BatchKitVo newKitPlan = newBatchKits.get(i);
            newKitPlan.setMkOrderId(mkOrderId);
            BigDecimal leadTime = BigDecimal.ZERO;
            if (IMKConstant.WHETHER_YES == newKitPlan.getNeedHandle()) {
                leadTime = processLeadTime;
            }

            // 计划开始时间
            LocalDateTime planStartDatetime = Optional.ofNullable(newKitPlan.getPlanStartTime()).orElse(now);
            newKitPlan.setPlanStartTime(mkCalendar.nextWorkDay(planStartDatetime, leadTime));

            // 对比 齐套时间 + 提前期、计划开始时间 + 提前期 、 报工时间  取Max + 提前期
            // 实际最早开始时间
            LocalDateTime kittingDate = newKitPlan.getKittingDate();
            LocalDateTime erlistStartTime = newKitPlan.getEarlistStartTime();
            erlistStartTime = mkCalendar.nextWorkDay(MinimaxUtil.maxNonNull(kittingDate, erlistStartTime), leadTime);
            newKitPlan.setEarlistStartTime(erlistStartTime);
        }

        kitInfo.put(mkOrderId, MRType.BY_START_DATE_REPORTED.merge(newBatchKits));
    }

    private Comparator<MKOrderVo> getBeSort() {
        return Comparator.comparing(MKOrderVo::getPrdProcessSeq)
                         .reversed()
                         .thenComparing(Comparator.comparing(MKOrderVo::getQty).reversed());
    }

    private List<BatchKitVo> followBefore(MKOrderVo mkOrder, List<BatchKitVo> beforeKits, List<BatchKitVo> batchKits) {
        if (CollectionUtil.isEmpty(beforeKits)) {
            beforeKits = CollectionUtil.newArrayList();
            BatchKitVo tempKit = new BatchKitVo();
            tempKit.setReported(IMKConstant.KIT_TYPE_STOCK);
            tempKit.setKittingQty(BigDecimal.ZERO);
            tempKit.setQty(BigDecimal.ZERO);
            beforeKits.add(tempKit);
        }
        if (CollectionUtil.isEmpty(batchKits)) {
            return CollectionUtil.newArrayList();
        }

        int index = 0;
        int beIndex = 0;
        BigDecimal planQty = mkOrder.getPlanQty();
        List<BatchKitVo> newKitPlans = CollectionUtil.newArrayList();
        BatchKitVo kitPlan = batchKits.get(index);
        BatchKitVo beKitPlan = beforeKits.get(beIndex);
        do {
            if (MathUtil.leZero(planQty)) {
                break;
            }
            if (beIndex < beforeKits.size()) {
                beKitPlan = beforeKits.get(beIndex);
            }
            if (index < batchKits.size()) {
                kitPlan = batchKits.get(index);
            }

            BigDecimal kitQty = kitPlan.getKittingQty();
            BigDecimal beKitQty = beKitPlan.getQty();
            BigDecimal kitDiff = MathUtil.subtract(beKitQty, kitQty);

            // 前工段齐套数量不足
            if (MathUtil.ltZero(kitDiff) && beIndex < beforeKits.size()) {
                BatchKitVo newKitPlan = buildKitPlan(kitPlan, beKitPlan);
                newKitPlans.add(newKitPlan);
                kitPlan.setKittingQty(kitDiff.abs());
                beKitPlan.setQty(BigDecimal.ZERO);
                planQty = MathUtil.subtract(planQty, beKitQty);
                beIndex++;
                continue;
            }

            // 前工段齐套数量超出
            if (MathUtil.gtZero(kitDiff) && index < batchKits.size()) {
                BatchKitVo newKitPlan = buildKitPlan(kitPlan, beKitPlan);
                newKitPlans.add(newKitPlan);
                beKitPlan.setQty(kitDiff);
                planQty = MathUtil.subtract(planQty, kitQty);
                index++;
                continue;
            }

            // 前工段齐套数量正好满足
            if (MathUtil.isZero(kitDiff)) {
                BatchKitVo newKitPlan = buildKitPlan(kitPlan, beKitPlan);
                beKitPlan.setQty(BigDecimal.ZERO);
                planQty = MathUtil.subtract(planQty, kitQty);
                newKitPlans.add(newKitPlan);
            }
            index++;
            beIndex++;
        } while (index < batchKits.size() || beIndex < beforeKits.size());
        return newKitPlans;
    }

    private BatchKitVo buildKitPlan(BatchKitVo kitPlan, BatchKitVo beKitPlan) {
        BatchKitVo newKitPlan = IMKMapper.INSTANCE.newArrivePlan(kitPlan);
        BigDecimal beQty = beKitPlan.getQty();
        BigDecimal kitQty = kitPlan.getKittingQty();
        newKitPlan.setKittingQty(MinimaxUtil.min(beQty, kitQty));
        newKitPlan.setPlanStartTime(MinimaxUtil.maxNonNull(beKitPlan.getPlanStartTime(), kitPlan.getPlanStartTime()));
        newKitPlan.setEarlistStartTime(MinimaxUtil.maxNonNull(beKitPlan.getEarlistStartTime(), kitPlan.getEarlistStartTime()));
        newKitPlan.setNeedHandle(IMKConstant.WHETHER_YES);

        // 本工段已报工, 不需要增加提前期并且开始时间 = 排程时间
        Boolean reported = IMKConstant.KIT_TYPE_REPORTED == kitPlan.getReported();
        if (reported) {
            newKitPlan.setNeedHandle(IMKConstant.WHETHER_NO);
        }
        if (!reported && IMKConstant.KIT_TYPE_REPORTED == beKitPlan.getReported()) {
            newKitPlan.setNeedHandle(IMKConstant.WHETHER_NO);
        }
        newKitPlan.setQty(newKitPlan.getKittingQty());
        return newKitPlan;
    }

    /**
     * 找出所有的分批齐套时间
     * Knitting不合单, 要计算不同尺码的各自齐套数量
     *
     * @param mkOrder
     * @param orders  前工段MK任务
     * @return 前工段分批齐套信息
     */
    private List<BatchKitVo> getBeforeKits(MKOrderVo mkOrder, List<MKOrderVo> orders) {
        if (CollectionUtil.isEmpty(orders)) {
            return CollectionUtil.newArrayList();
        }
        Map<String, List<MKOrderVo>> orderMap = CollectionUtil.group(orders, o -> o.mrKey() + o.getSize());
        List<BatchKitVo> beforeKits = new ArrayList<>();
        for (Map.Entry<String, List<MKOrderVo>> entry : orderMap.entrySet()) {
            var sortOrders = entry.getValue().stream().sorted(getBeSort()).collect(Collectors.toList());
            List<List<BatchKitVo>> taskKits = CollectionUtil.map(sortOrders, task -> {
                return kitInfo.get(task.getId());
            });
            CollectionUtil.orAddAll(beforeKits, CollectionUtil.first(taskKits));
        }
        List<BatchKitVo> mrBeforeKits = MRType.BY_ORDER_REPORTED.merge(beforeKits);

        // Knitting工段齐套数量按照比例分配到各个尺码
        BigDecimal beforeQty = CollectionUtil.first(orders).getPlanQty();
        BigDecimal proportion = BigDecimal.ONE;
        BigDecimal qty = mkOrder.getPlanQty();
        if (!MathUtil.isZero(beforeQty) && !MathUtil.isZero(qty)) {
            proportion = qty.divide(beforeQty, 6, RoundingMode.HALF_UP);
        }

        List<BatchKitVo> newMRBeforeKits = CollectionUtil.newArrayList();
        BigDecimal shortCnt = BigDecimal.ZERO;
        for (BatchKitVo beforeKit : mrBeforeKits) {
            BatchKitVo newBeforeKit = IMKMapper.INSTANCE.newArrivePlan(beforeKit);
            BigDecimal propKitQty = beforeKit.getKittingQty().multiply(proportion).setScale(0, RoundingMode.HALF_UP);
            if (MathUtil.isZero(propKitQty)) {
                shortCnt = shortCnt.add(BigDecimal.ONE);
            }
            newBeforeKit.setKittingQty(propKitQty);
            newBeforeKit.setQty(propKitQty);
            newMRBeforeKits.add(newBeforeKit);
        }

        // 有短缺数量，从最大齐套批次补
        if (!MathUtil.isZero(shortCnt)) {
            var maxKit = CollectionUtil.last(CollectionUtil.sort(newMRBeforeKits, BatchKitVo::getKittingQty));
            maxKit.setKittingQty(maxKit.getKittingQty().add(shortCnt));
        }
        return newMRBeforeKits;
    }
}
