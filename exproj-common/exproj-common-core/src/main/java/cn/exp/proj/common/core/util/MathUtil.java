package cn.exp.proj.common.core.util;

import cn.exp.proj.common.core.asserts.ProjAssert;
import cn.exp.proj.common.core.asserts.error.IError;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class MathUtil {
    /**
     * 返回列表内某个BigDecimal类型字段的累加和
     *
     * @param collection
     * @param mapper
     * @param <T>
     * @return
     */
    public static <T> BigDecimal sum(Collection<T> collection, Function<T, BigDecimal> mapper) {
        if (CollectionUtil.isEmpty(collection)) {
            return BigDecimal.ZERO;
        }
        return collection.stream().map(mapper).reduce(MathUtil::add).orElse(BigDecimal.ZERO);
    }

    /**
     * 返回两个数的和
     * null = 0
     *
     * @param x
     * @param y
     * @return x+y
     */
    public static BigDecimal add(BigDecimal x, BigDecimal y) {
        Optional<BigDecimal> sumOpt = Optional.ofNullable(x);
        if (Objects.isNull(y)) {
            return sumOpt.orElse(BigDecimal.ZERO);
        }
        return sumOpt.map(y::add).orElse(y);
    }

    /**
     * 返回两个数的差
     * null = 0
     *
     * @param x
     * @param y
     * @return x - y
     */
    public static BigDecimal subtract(BigDecimal x, BigDecimal y) {
        Optional<BigDecimal> remainderOpt = Optional.ofNullable(y);
        if (Objects.isNull(x)) {
            return remainderOpt.orElse(BigDecimal.ZERO);
        }
        return remainderOpt.map(x::subtract).orElse(x);
    }

    /**
     * Double 转 BigDecimal<br/>
     * 为空返回 BigDecimal.ZERO
     *
     * @param num
     * @return
     */
    public static BigDecimal valueOf(Double num) {
        return Optional.ofNullable(num).map(BigDecimal::valueOf).orElse(BigDecimal.ZERO);
    }

    /**
     * 为空返回 BigDecimal.ZERO
     *
     * @param decimal
     * @return
     */
    public static BigDecimal orZero(BigDecimal decimal) {
        return Optional.ofNullable(decimal).orElse(BigDecimal.ZERO);
    }

    /**
     * 判断整数是否相等
     *
     * @param x
     * @param y
     * @return
     */
    public static Boolean intEq(BigDecimal x, BigDecimal y) {
        return longEq(x, y);
    }

    /**
     * 判断整数是否相等
     *
     * @param x
     * @param y
     * @return
     */
    public static Boolean longEq(BigDecimal x, BigDecimal y) {
        if (Objects.isNull(x) || Objects.isNull(y)) {
            return Boolean.FALSE;
        }
        return x.longValue() == y.longValue();
    }

    public static boolean isZero(BigDecimal x) {
        ProjAssert.nonNull(x, IError.UNKNOWN, "数值判断不可为空");
        return x.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 小于零
     *
     * @param x
     * @return 小于零
     */
    public static boolean ltZero(BigDecimal x) {
        ProjAssert.nonNull(x, IError.UNKNOWN, "数值判断不可为空");
        return x.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * 小于零
     *
     * @param x
     * @return 小于零
     */
    public static boolean ltZero(long x) {
        return x < 0;
    }

    /**
     * 小于等于零
     *
     * @param x
     * @return 小于等于零
     */
    public static boolean leZero(BigDecimal x) {
        ProjAssert.nonNull(x, IError.UNKNOWN, "数值判断不可为空");
        return x.compareTo(BigDecimal.ZERO) <= 0;
    }

    /**
     * 小于零
     *
     * @param x
     * @return 小于零
     */
    public static boolean leZero(long x) {
        return x <= 0;
    }

    /**
     * 大于零
     *
     * @param x
     * @return 大于零
     */
    public static boolean gtZero(BigDecimal x) {
        ProjAssert.nonNull(x, IError.UNKNOWN, "数值判断不可为空");
        return x.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean gtZero(long x) {
        return x > 0;
    }

    /**
     * 大于等于零
     *
     * @param x
     * @return 大于等于零
     */
    public static boolean geZero(BigDecimal x) {
        ProjAssert.nonNull(x, IError.UNKNOWN, "数值判断不可为空");
        return x.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean geZero(long x) {
        return x >= 0;
    }
}
