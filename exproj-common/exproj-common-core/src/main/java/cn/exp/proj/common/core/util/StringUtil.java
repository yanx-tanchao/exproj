package cn.exp.proj.common.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class StringUtil {
    private StringUtil() {
    }

    /**
     * 中横线
     */
    public static final String HYPHEN = "-";

    /**
     * 逗号
     */
    public static final String COMMA = ",";

    /**
     * 空字符串
     */
    public static final String EMPTY = "";

    /**
     * 点
     */
    public static final String POINT = ".";

    private static final Supplier<ObjectMapper> SNAKE_JSON_MAPPER = Suppliers.memoize(() -> {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper;
    });

    /**
     * 组装字符串
     * 默认用中划线拼接
     *
     * @param values
     * @return
     */
    public static String join(Object... values) {
        if (Objects.isNull(values)) {
            return EMPTY;
        }
        StringBuilder builder = new StringBuilder();
        for (Object value : values) {
            if (Objects.isNull(value)) {
                continue;
            }
            builder.append(value);
            builder.append(HYPHEN);
        }
        return builder.substring(0, builder.lastIndexOf(HYPHEN));
    }

    /**
     * <p>判断两个非空字符串是否相等.
     * <p>有一个为null则返回False.
     * <p>仅支持判断两个不为空的字符串.
     *
     * @param text1
     * @param text2
     * @return true or false
     */
    public static Boolean eq(String text1, String text2) {
        return Optional.ofNullable(text1).map(t -> t.equals(text2)).orElse(Boolean.FALSE);
    }

    public static boolean isBlank(String text) {
        return Objects.isNull(text) || "".equals(text.trim());
    }

    public static boolean isNotBlank(String text) {
        return !isBlank(text);
    }

    public static String toSnakeJson(Object o) {
        try {
            return SNAKE_JSON_MAPPER.get().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.debug("对象转Json字符串异常。。。");
        }
        return EMPTY;
    }
}
