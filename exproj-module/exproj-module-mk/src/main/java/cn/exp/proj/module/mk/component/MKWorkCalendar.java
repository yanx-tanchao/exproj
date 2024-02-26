package cn.exp.proj.module.mk.component;

import cn.exp.proj.common.core.util.CollectionUtil;
import cn.exp.proj.common.core.util.MathUtil;
import cn.exp.proj.common.core.util.TimeUtil;
import cn.exp.proj.common.core.wrapper.BooleanWrapper;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 工作日历
 * 不考虑多线程
 */
public class MKWorkCalendar {
    // 正序工作日
    @Setter
    private List<LocalDateTime> workDays;

    // 缓存查找结果
    private Map<WorkDayCacheKey, LocalDateTime> cacheWorkDay = new HashMap<>();

    // 倒序工作日
    @Setter
    private List<LocalDateTime> reversedWorkDays;

    // 缓存查找结果
    private Map<ReversedWorkDayCacheKey, LocalDateTime> cacheReversedWorkDay = new HashMap<>();

    /**
     * 工作日后推
     *
     * @param localDateTime 计算时间
     * @param offset        找到工作日后再往后延期N个工作日
     * @return
     */
    public LocalDateTime nextWorkDay(LocalDateTime localDateTime, BigDecimal offset) {
        WorkDayCacheKey cacheKey = new WorkDayCacheKey(localDateTime, offset);
        LocalDateTime nextDay = cacheWorkDay.get(cacheKey);
        if (Objects.nonNull(nextDay)) {
            return nextDay;
        }
        nextDay = findNextWorkDay(workDays, localDateTime, offset);
        cacheWorkDay.put(cacheKey, nextDay);
        return nextDay;
    }

    private record WorkDayCacheKey(LocalDateTime localDateTime, BigDecimal later) {

    }

    private record ReversedWorkDayCacheKey(LocalDateTime localDateTime, BigDecimal lt) {

    }

    /**
     * 工作日前推
     *
     * @param localDateTime 计算时间
     * @param lt            提前期
     * @return
     */
    public LocalDateTime preWorkDay(LocalDateTime localDateTime, BigDecimal lt) {
        ReversedWorkDayCacheKey cacheKey = new ReversedWorkDayCacheKey(localDateTime, lt);
        LocalDateTime nextDay = cacheReversedWorkDay.get(cacheKey);
        if (Objects.nonNull(nextDay)) {
            return nextDay;
        }
        nextDay = findPreWorkDay(reversedWorkDays, localDateTime, lt);
        cacheReversedWorkDay.put(cacheKey, nextDay);
        return nextDay;
    }


    /**
     * 按照给出的时间点和提前期查找前工作 日
     * 并根据给出的时间间隔继续推算时间
     *
     * @param days
     * @param time
     * @param interval
     * @return
     */
    private LocalDateTime findPreWorkDay(List<LocalDateTime> days, LocalDateTime time, BigDecimal interval) {
        List<LocalDateTime> dichotomicTimes = CollectionUtil.map(days);
        BigDecimal realIntervalDeci = interval.setScale(0, RoundingMode.DOWN);

        // 整数位
        Long realInterval = realIntervalDeci.longValue();

        // 小数位
        BigDecimal point = interval.subtract(realIntervalDeci);
        BigDecimal newTimeMinute = BigDecimal.valueOf(time.toLocalTime().toSecondOfDay() / 60);
        BigDecimal intervalMinute = point.multiply(BigDecimal.valueOf(TimeUtil.DAY_MINUTES));
        BigDecimal minuteDiff = newTimeMinute.subtract(intervalMinute);
        if (MathUtil.ltZero(minuteDiff)) {
            realInterval = realInterval + 1;
        }

        while (checkDichotomic(dichotomicTimes, realInterval)) {
            dichotomicTimes = timeDichotomy1(dichotomicTimes, time, realInterval);
        }

        dichotomicTimes.removeIf(dateTime -> dateTime.toLocalDate().compareTo(time.toLocalDate()) > 0);

        // 找不到前一个工作日了
        if (CollectionUtil.isEmpty(dichotomicTimes)) {
            return time;
        }
        LocalDateTime dateTime = dichotomicTimes.get(realInterval.intValue());

        BigDecimal fractionalPart = minuteDiff;
        if (MathUtil.ltZero(minuteDiff)) {
            fractionalPart = TimeUtil.DAY_MINUTES_DECIMAL.add(minuteDiff);
        }

        return dateTime.plusMinutes(fractionalPart.longValue());
    }


    /**
     * 按照给出的时间点和提前期查找前工作日
     * 并根据给出的时间间隔继续推算时间
     *
     * @param days   所有工作日
     * @param time   查询时间
     * @param offset 工作日偏移量
     * @return 查询时间的下一个工作日, 无偏移量则返回的第一天工作日(包含查询时间正好是工作日)
     */
    private LocalDateTime findNextWorkDay(List<LocalDateTime> days, LocalDateTime time, BigDecimal offset) {
        List<LocalDateTime> dichotomicTimes = CollectionUtil.map(days);

        // 小数位
        BigDecimal timeHour = BigDecimal.valueOf(time.getHour());
        BigDecimal timeDayRatio = timeHour.divide(TimeUtil.DAY_HOURS_DECIMAL, 2, RoundingMode.UP);
        BigDecimal realOffset = offset.add(timeDayRatio);

        // 整数位
        Long realInterval = realOffset.longValue();
        BigDecimal moreHours = realOffset.subtract(BigDecimal.valueOf(realInterval));

        while (checkDichotomic(dichotomicTimes, realInterval)) {
            dichotomicTimes = timeDichotomy(dichotomicTimes, time, realInterval);
        }
        dichotomicTimes.removeIf(dateTime -> dateTime.toLocalDate().compareTo(time.toLocalDate()) < 0);

        int index = realInterval.intValue();
        Boolean outOfWorkDay = dichotomicTimes.size() <= index;
        LocalDateTime dateTime = outOfWorkDay ? CollectionUtil.last(dichotomicTimes): dichotomicTimes.get(index);

        return dateTime.plusHours(moreHours.multiply(TimeUtil.DAY_HOURS_DECIMAL).longValue());
    }

    /**
     * 判断是否可以继续二分
     *
     * @param dichotomicTimes
     * @param interval
     * @return
     */
    private boolean checkDichotomic(List<LocalDateTime> dichotomicTimes, Long interval) {
        BooleanWrapper wrapper = new BooleanWrapper(CollectionUtil.isNotEmpty(dichotomicTimes));

        // 二分后至少保证(区间个数 + 1) * 2,
        wrapper.and(dichotomicTimes.size() > (interval + 1) * 2 * 2);

        return wrapper.eval();
    }

    private List<LocalDateTime> timeDichotomy1(List<LocalDateTime> days, LocalDateTime time, Long interval) {
        int dichtomic = days.size() / 2;
        List<LocalDateTime> right = CollectionUtil.skip(days, dichtomic);
        LocalDateTime rightStart = CollectionUtil.first(right);
        List<LocalDateTime> left = CollectionUtil.limit(days, dichtomic);
        LocalDateTime leftEnd = CollectionUtil.last(left);
        if (leftEnd.toLocalDate().compareTo(time.toLocalDate()) > 0) {
            return right;
        }

        // 二分如果是取左边， 考虑可能出现尾部时间， 至少保证区间时间数量
        return CollectionUtil.limit(days, dichtomic + (interval + 1) * 2);
    }

    private List<LocalDateTime> timeDichotomy(List<LocalDateTime> days, LocalDateTime time, Long interval) {
        int dichtomic = days.size() / 2;
        List<LocalDateTime> left = CollectionUtil.limit(days, dichtomic);
        List<LocalDateTime> right = CollectionUtil.skip(days, dichtomic);
        LocalDateTime leftEnd = CollectionUtil.last(left);
        if (leftEnd.toLocalDate().compareTo(time.toLocalDate()) < 0) {
            return right;
        }

        // 二分如果是取左边， 考虑可能出现尾部时间， 至少保证区间时间数量
        return CollectionUtil.limit(days, dichtomic + (interval + 1) * 2);
    }
}
