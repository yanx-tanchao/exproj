package cn.exp.proj.module.mk.component.util;

import cn.exp.proj.common.core.util.CollectionUtil;
import cn.exp.proj.common.core.util.MathUtil;
import cn.exp.proj.module.mk.vo.BatchKitVo;
import cn.exp.proj.module.mk.mapper.IMKMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum MRType {
    BY_KIT_DATE(MRType::handleByKitDate),
    BY_START_DATE(MRType::handleEmpty),
    BY_TASK(MRType::handleEmpty),
    BY_REPORTED(MRType::handleByKitDate),
    BY_ORDER_REPORTED(MRType::handleEmpty),
    BY_START_DATE_REPORTED(MRType::handleEmpty);

    private BiConsumer<BatchKitVo, BatchKitVo> handler;

    public KitInfo keyApply(BatchKitVo kitPlan) {
        LocalDateTime kitDate = LocalDateTime.of(kitPlan.getKittingDate().toLocalDate(), LocalTime.MIN);
        LocalDateTime startDate = kitPlan.getEarlistStartTime();
        if (BY_KIT_DATE.equals(this) || BY_REPORTED.equals(this)) {
            startDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        }

        StringBuilder builder = new StringBuilder();
        if (BY_REPORTED.equals(this) || BY_ORDER_REPORTED.equals(this) || BY_START_DATE_REPORTED.equals(this)) {
            builder.append(kitPlan.getReported());
        }

        if (BY_TASK.equals(this)) {
            builder.append(kitPlan.getMkOrderTaskId());
        }

        if (BY_ORDER_REPORTED.equals(this)) {
            builder.append(kitPlan.getMkOrderId());
        }

        return new KitInfo(kitDate, startDate, builder.toString());
    }

    public List<BatchKitVo> merge(List<BatchKitVo> arrivePlans) {
        return MRType.merge(arrivePlans, this);
    }

    record KitInfo(LocalDateTime kitDate, LocalDateTime startTime, String ohterCond) {
    }

    private static void handleByKitDate(BatchKitVo kitPlan, BatchKitVo mergePlan) {
        kitPlan.setEarlistStartTime(mergePlan.getEarlistStartTime());
        kitPlan.setPlanStartTime(kitPlan.getKittingDate());
    }

    private static void handleEmpty(BatchKitVo kitPlan, BatchKitVo mergePlan) {
    }

    private static List<BatchKitVo> merge(List<BatchKitVo> arrivePlans, MRType mrType) {
        if (CollectionUtil.isEmpty(arrivePlans)) {
            return CollectionUtil.newArrayList();
        }

        Map<KitInfo, List<BatchKitVo>> dateMap = CollectionUtil.group(arrivePlans, mrType::keyApply);
        List<BatchKitVo> newKitPlans = new ArrayList<>();
        for (Map.Entry<KitInfo, List<BatchKitVo>> dateRecord : dateMap.entrySet()) {
            List<BatchKitVo> sameDateArrivePlans = dateRecord.getValue();
            BatchKitVo kitPlan = CollectionUtil.first(sameDateArrivePlans);
            BatchKitVo newKitPlan = IMKMapper.INSTANCE.newMRKitPlan(kitPlan);
            newKitPlan.setKittingDate(dateRecord.getKey().kitDate());
            newKitPlan.setKittingQty(MathUtil.sum(sameDateArrivePlans, BatchKitVo::getKittingQty));
            newKitPlan.setQty(newKitPlan.getKittingQty());
            newKitPlan.setPlanStartTime(kitPlan.getPlanStartTime());
            newKitPlan.setEarlistStartTime(dateRecord.getKey().startTime());

            Optional.ofNullable(mrType.getHandler()).ifPresent(h -> h.accept(newKitPlan, kitPlan));

            newKitPlans.add(newKitPlan);
        }
        return newKitPlans.stream().sorted(sortCompare()).collect(Collectors.toList());
    }

    private static Comparator<BatchKitVo> sortCompare() {
        LocalDateTime now = LocalDateTime.now();
        return Comparator.comparing(BatchKitVo::getKittingDate).thenComparing(t -> {
            return Optional.ofNullable(t.getEarlistStartTime()).orElse(now);
        });
    }
}
