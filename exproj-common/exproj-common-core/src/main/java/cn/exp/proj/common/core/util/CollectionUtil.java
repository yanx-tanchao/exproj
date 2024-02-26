package cn.exp.proj.common.core.util;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 集合相关工具类
 */
public class CollectionUtil {
    private CollectionUtil() {
    }

    /**
     * 集合为空
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }

    /**
     * 集合不为空
     *
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * map为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return CollectionUtils.isEmpty(map);
    }

    /**
     * map不为空
     *
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <T> boolean anyMatch(Collection<T> datas, Predicate<T> predicate) {
        if (isEmpty(datas)) {
            return Boolean.FALSE;
        }
        return datas.stream().anyMatch(predicate);
    }

    public static <T> List<T> map(Collection<T> datas) {
        return map(datas, Function.identity());
    }

    public static <T, R, K> R groupAndThen(Collection<T> datas, Function<T, K> mapper, Function<Map<K, List<T>>, R> finisher) {
        if (isEmpty(datas)) {
            return null;
        }
        return datas.stream().collect(Collectors.collectingAndThen(Collectors.groupingBy(mapper), finisher));
    }

    /**
     * Collection转换器
     * List<T> -> List<R>
     *
     * @param datas  转换前数据
     * @param mapper 转换函数
     * @param <T>    入参类型
     * @param <R>    出参类型
     * @return 转换后数据
     */
    public static <T, R> List<R> map(Collection<T> datas, Function<T, R> mapper) {
        if (isEmpty(datas)) {
            return newArrayList();
        }
        return datas.stream().map(mapper).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static <T, R> Set<R> mapToSet(Collection<T> datas, Function<T, R> mapper) {
        if (isEmpty(datas)) {
            return new HashSet<>();
        }
        return datas.stream().map(mapper).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * 集合去重
     *
     * @param datas 去重前数据
     * @param <T>   参数类型 extends Comparable
     * @return 去重后数据
     */
    public static <T extends Comparable> List<T> distinct(Collection<T> datas) {
        return distinct(datas, Function.identity());
    }

    /**
     * 集合去重
     *
     * @param datas  去重前数据
     * @param mapper 按某个字段去重
     * @param <T>    参数类型
     * @param <R>    去重字段的类型
     * @return 去重后数据
     */
    public static <T, R extends Comparable> List<T> distinct(Collection<T> datas, Function<T, R> mapper) {
        if (isEmpty(datas)) {
            return newArrayList();
        }
        return datas.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> {
            return new TreeSet<T>(Comparator.comparing(mapper));
        }), CollectionUtil::map));
    }

    /**
     * 聚合集合
     *
     * @param datas      集合
     * @param aggregator 聚合函数
     * @param <T>
     * @return
     */
    public static <T> T reduce(Collection<T> datas, BinaryOperator<T> aggregator) {
        if (isEmpty(datas)) {
            return null;
        }
        return datas.stream().reduce(aggregator::apply).get();
    }

    /**
     * 聚合集合Map的Values
     *
     * @param map
     * @param aggregator 聚合函数
     * @param <T>        出入参类型
     * @return
     */
    public static <T> List<T> reduce(Map<?, List<T>> map, BinaryOperator<T> aggregator) {
        if (isEmpty(map)) {
            return newArrayList();
        }
        return map(map.values(), list -> reduce(list, aggregator));
    }

    /**
     * Collection转换器
     * List<List<T>> -> List<T>
     *
     * @param datas  转换前数据
     * @param mapper 转换函数
     * @param <T>    入参类型
     * @return 转换后数据
     */
    public static <T> List<T> flatMap(Collection<List<T>> datas) {
        return flatMap(datas, Function.identity());
    }

    /**
     * Collection转换器
     * List<List<T>> -> List<T>
     *
     * @param datas  转换前数据
     * @param mapper 转换函数
     * @param <T>    入参类型
     * @return 转换后数据
     */
    public static <T, R> List<R> flatMap(Collection<T> datas, Function<T, List<R>> mapper) {
        if (isEmpty(datas)) {
            return newArrayList();
        }
        return datas.stream().map(mapper).filter(Objects::nonNull).flatMap(Collection::stream).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Collection转换器
     * Map<Object,List<T>> -> List<T>
     *
     * @param datas  转换前数据
     * @param mapper 转换函数
     * @param <T>    入参类型
     * @return 转换后数据
     */
    public static <T> List<T> flatMap(Map<?, List<T>> datas) {
        if (isEmpty(datas)) {
            return newArrayList();
        }
        return flatMap(datas.values());
    }

    /**
     * Collection转换器
     * List<T> -> List<R>
     *
     * @param datas  转换前数据
     * @param mapper 转换函数
     * @param <T>    入参类型
     * @param <R>    出参类型
     * @return 转换后数据
     */
    public static <T, R> Map<R, T> toMap(Collection<T> datas, Function<T, R> mapper) {
        return toMap(datas, mapper, Function.identity());
    }

    /**
     * Collection转换器
     * List<T> -> Map<R,U>
     * 有重复值 (v1,v2) -> v1
     *
     * @param datas 转换前数据
     * @param key   Key转换器
     * @param value value转换器
     * @param <T>   入参类型
     * @param <R>   Map-Key
     * @param <U>   Map-Value
     * @return 转换后数据
     */
    public static <T, R, U> Map<R, U> toMap(Collection<T> datas, Function<T, R> key, Function<T, U> value) {
        if (isEmpty(datas)) {
            return newHashMap();
        }
        return datas.stream().collect(Collectors.toMap(key, value, (v1, v2) -> v1));
    }

    /**
     * 分组
     *
     * @param datas  集合
     * @param mapper Map-key
     * @param <T>
     * @return
     */
    public static <T, R> Map<R, List<T>> group(Collection<T> datas, Function<T, R> mapper) {
        if (isEmpty(datas)) {
            return newHashMap();
        }
        return datas.stream().collect(Collectors.groupingBy(mapper));
    }

    /**
     * 分组为LinkedHashMap
     *
     * @param datas  集合
     * @param mapper Map-key
     * @param <T>
     * @return
     */
    public static <T, R> Map<R, List<T>> linkGroup(Collection<T> datas, Function<T, R> mapper) {
        if (isEmpty(datas)) {
            return newHashMap();
        }
        return datas.stream().collect(Collectors.groupingBy(mapper, LinkedHashMap::new, Collectors.toList()));
    }

    public static <T, R> Set<R> toSet(Collection<T> datas, Function<T, R> mapper) {
        if (isEmpty(datas)) {
            return newHashSet();
        }
        return datas.stream().map(mapper).collect(Collectors.toSet());
    }

    /**
     * 排序 - 正序
     *
     * @param datas
     * @param <T>
     * @return
     */
    public static <T extends Comparable<? super T>> List<T> sort(Collection<T> datas) {
        return sort(datas, Function.identity());
    }

    public static <T, R extends Comparable<? super R>> List<T> sort(Collection<T> datas, Function<T, R> mapper) {
        if (isEmpty(datas)) {
            return newArrayList();
        }
        return datas.stream().sorted(Comparator.comparing(mapper)).collect(Collectors.toList());
    }

    /**
     * 排序 - 倒序
     *
     * @param datas
     * @param <T>
     * @return
     */
    public static <T extends Comparable<? super T>> List<T> reversed(Collection<T> datas) {
        return reversed(datas, Function.identity());
    }

    public static <T, R extends Comparable<? super R>> List<T> reversed(Collection<T> datas, Function<T, R> mapper) {
        if (isEmpty(datas)) {
            return newArrayList();
        }
        return datas.stream().sorted(Comparator.comparing(mapper).reversed()).collect(Collectors.toList());
    }

    public static <T> List<T> filter(Collection<T> datas, Predicate<T> predicate) {
        if (isEmpty(datas)) {
            return newArrayList();
        }
        return datas.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <T> T filterFirst(Collection<T> datas, Predicate<T> predicate) {
        if (isEmpty(datas)) {
            return null;
        }
        return datas.stream().filter(predicate).findFirst().orElse(null);
    }

    public static <T> List<T> orElse(List<T> list) {
        return Optional.ofNullable(list).orElseGet(CollectionUtil::newArrayList);
    }


    /**
     * 往集合里添加对象， 集合==null则newArrayList
     *
     * @param list
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<T> orAdd(List<T> list, T t) {
        List<T> result = orElse(list);
        Optional.ofNullable(t).ifPresent(result::add);
        return result;
    }

    public static <T> List<T> orAddAll(List<T> list, List<T> t) {
        List<T> result = orElse(list);
        Optional.ofNullable(t).ifPresent(result::addAll);
        return result;
    }

    public static <T> List<T> limit(List<T> list, long size) {
        if (isEmpty(list)) {
            return newArrayList();
        }
        return list.stream().limit(size).collect(Collectors.toList());
    }

    public static <T> List<T> skip(List<T> list, long index) {
        if (isEmpty(list)) {
            return newArrayList();
        }
        return list.stream().skip(index).collect(Collectors.toList());
    }

    public static boolean contains(Collection<?> collection, Object value) {
        return isNotEmpty(collection) && collection.contains(value);
    }

    public static <T> List<T> newArrayList() {
        return new ArrayList<T>();
    }

    public static <T> Set<T> newHashSet() {
        return new HashSet<>();
    }

    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    public static <T> T first(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    public static <T> T last(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }
}
