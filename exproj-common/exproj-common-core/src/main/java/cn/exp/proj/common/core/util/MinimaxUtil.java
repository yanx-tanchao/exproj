package cn.exp.proj.common.core.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

/**
 * 取最大最小的工具类
 */
public class MinimaxUtil {
    /**
     * 取最大值
     *
     * @param <T> 返回值类型
     * @return 最大值
     */
    public static <T extends Comparable> T max(Collection<T> collection) {
        return max(collection, Function.identity());
    }

    /**
     * 取最大值
     *
     * @param mapper 转换方法
     * @param <T>    转换方法入参
     * @param <R>    转换方法出参和返回值类型
     * @return 最大值
     */
    public static <T, R extends Comparable> R max(Collection<T> collection, Function<T, R> mapper) {
        if (CollectionUtil.isEmpty(collection)) {
            return null;
        }
        return (R) collection.stream()
                             .map(mapper)
                             .filter(Objects::nonNull)
                             .max(Comparator.comparing(Function.identity()))
                             .get();
    }

    /**
     * 取最小值
     *
     * @param <T> 返回值类型
     * @return 最小值
     */
    public static <T extends Comparable> T min(Collection<T> collection) {
        return min(collection, Function.identity());
    }

    /**
     * 取最小值
     *
     * @param mapper 转换方法
     * @param <T>    转换方法入参
     * @param <R>    转换方法出参和返回值类型
     * @return 最小值
     */
    public static <T, R extends Comparable> R min(Collection<T> collection, Function<T, R> mapper) {
        if (CollectionUtil.isEmpty(collection)) {
            return null;
        }
        return (R) collection.stream().map(mapper).min(Comparator.comparing(Function.identity())).get();
    }

    /**
     * 取最大值
     *
     * @param first
     * @param second
     * @param <T>    返回值类型
     * @return 最大值
     */
    public static <T extends Comparable> T max(T first, T second) {
        return first.compareTo(second) > 0 ? first : second;
    }

    /**
     * 有一个null， 则返回另外一个值
     * 先判断first，再判断second
     *
     * @param first
     * @param second
     * @param <T>
     * @return
     */
    public static <T extends Comparable> T maxNonNull(T first, T second) {
        if (Objects.isNull(first)) {
            return second;
        }
        if (Objects.isNull(second)) {
            return first;
        }
        return max(first, second);
    }

    /**
     * 取最小值
     *
     * @param first
     * @param second
     * @param <T>    返回值类型
     * @return 最小值
     */
    public static <T extends Comparable> T min(T first, T second) {
        return first.compareTo(second) < 0 ? first : second;
    }
}
