package cn.exp.proj.common.core.asserts;

import cn.exp.proj.common.core.asserts.error.ErrorInfo;
import cn.exp.proj.common.core.asserts.error.IError;
import cn.exp.proj.common.core.exceptions.BizException;
import cn.exp.proj.common.core.util.CollectionUtil;
import cn.exp.proj.common.core.util.StringUtil;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * HG断言工具类<br/>
 * 支持集合，Map等对象的判空断言<br/>
 * 支持国际化<br/>
 * 默认抛出未知异常{@link UnknownError}<br/>
 * 可自定以Error枚举实现{@link IError}接口<br/>
 */
@Slf4j
public class ProjAssert {
    public static final Supplier<MessageSource> messageSource = Suppliers.memoize(() -> {
        return null;
    });

    /**
     * obj = null 则抛出异常
     *
     * @param obj
     */
    public static void nonNull(Object obj) {
        nonNull(obj, UnknownError.DATA_ERROR);
    }

    public static void nonNull(Object obj, IError error) {
        nonNull(obj, error, null);
    }

    public static void nonNull(Object obj, IError error, Object... params) {
        orThrow(Optional.ofNullable(obj), error, params);
    }

    /**
     * obj = null 则抛出异常
     * 支持直传code和message
     * 建议使用枚举：能从枚举命名上看出来是属于什么错误
     *
     * @param obj
     * @param code
     * @param message
     */
    public static void nonNull(Object obj, Integer code, String message) {
        nonNull(obj, code, message, null);
    }

    public static void nonNull(Object obj, Integer code, String message, Object... params) {
        nonNull(obj, ErrorInfo.of(code, message), params);
    }

    /**
     * collection 为空 抛出异常
     *
     * @param collection
     */
    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, UnknownError.DATA_ERROR);
    }

    public static void notEmpty(Collection<?> collection, IError error) {
        notEmpty(collection, error, null);
    }

    public static void notEmpty(Collection<?> collection, IError error, Object... params) {
        orThrow(Optional.ofNullable(collection).filter(CollectionUtil::isNotEmpty), error, params);
    }

    public static void notEmpty(Collection<?> collection, Integer code, String message) {
        notEmpty(collection, code, message, null);
    }

    public static void notEmpty(Collection<?> collection, Integer code, String message, Object... params) {
        notEmpty(collection, ErrorInfo.of(code, message), params);
    }

    /**
     * collection 不为空 抛出异常
     *
     * @param collection
     * @param error
     */
    public static void isEmpty(Collection<?> collection, IError error) {
        isEmpty(collection, error, null);
    }

    public static void isEmpty(Collection<?> collection, IError error, Object... params) {
        if (CollectionUtil.isNotEmpty(collection)) {
            String errMsg = handleI18n(error, params);
            log.error("数据校验异常. Error:{}, message: {}", error.getCode(), errMsg);
            throw new BizException(error.getCode(), errMsg);
        }
    }

    public static void isEmpty(Collection<?> collection, Integer code, String message) {
        isEmpty(collection, code, message, null);
    }

    public static void isEmpty(Collection<?> collection, Integer code, String message, Object... params) {
        isEmpty(collection, ErrorInfo.of(code, message), params);
    }


    /**
     * collection 不为空 抛出异常
     *
     * @param collection
     * @param error
     */
    public static void isEmpty(Object obj, IError error) {
        isEmpty(obj, error, null);
    }

    /**
     * collection 不为空 抛出异常
     *
     * @param collection
     * @param error
     */
    public static void isEmpty(Object obj, IError error, Object... params) {
        if (Objects.isNull(obj)) {
            String errMsg = handleI18n(error, params);
            log.error("数据校验异常. Error:{}, message: {}", error.getCode(), errMsg);
            throw new BizException(error.getCode(), errMsg);
        }
    }

    /**
     * map 为空 则抛出异常
     *
     * @param collection
     */
    public static void notEmpty(Map<?, ?> map) {
        notEmpty(map, UnknownError.DATA_ERROR);
    }

    public static void notEmpty(Map<?, ?> map, IError error) {
        notEmpty(map, error, null);
    }

    public static void notEmpty(Map<?, ?> map, IError error, Object... params) {
        orThrow(Optional.ofNullable(map).filter(CollectionUtil::isNotEmpty), error, params);
    }

    public static void notEmpty(Map<?, ?> map, Integer code, String message) {
        notEmpty(map, code, message, null);
    }

    public static void notEmpty(Map<?, ?> map, Integer code, String message, Object... params) {
        notEmpty(map, ErrorInfo.of(code, message), params);
    }

    /**
     * 字符串 为空 则抛出异常
     *
     * @param text
     */
    public static void notBlank(String text) {
        notBlank(text, UnknownError.DATA_ERROR);
    }

    public static void notBlank(String text, IError error) {
        notBlank(text, error, null);
    }

    public static void notBlank(String text, IError error, Object... params) {
        orThrow(Optional.ofNullable(text).filter(StringUtil::isNotBlank), error, params);
    }

    public static void notBlank(String text, Integer code, String message) {
        notBlank(text, code, message, null);
    }

    public static void notBlank(String text, Integer code, String message, Object... params) {
        notBlank(text, ErrorInfo.of(code, message), params);
    }


    /**
     * bool == false 则抛出异常
     *
     * @param bool
     */
    public static void assertTrue(boolean bool) {
        assertTrue(bool, UnknownError.DATA_ERROR);
    }

    public static void assertTrue(boolean bool, IError error) {
        assertTrue(bool, error, null);
    }

    public static void assertTrue(boolean bool, IError error, Object... params) {
        if (bool) {
            String errMsg = handleI18n(error, params);
            log.error("数据校验异常. Error:{}, message: {}", error.getCode(), errMsg);
            throw new BizException(error.getCode(), errMsg);
        }
    }

    public static void assertTrue(boolean bool, Integer code, String message) {
        assertTrue(bool, code, message, null);
    }

    public static void assertTrue(boolean bool, Integer code, String message, Object... params) {
        assertTrue(bool, ErrorInfo.of(code, message), params);
    }

    /**
     * bool == true 则抛出异常
     *
     * @param bool
     */
    public static void assertFalse(boolean bool) {
        assertFalse(bool, UnknownError.DATA_ERROR);
    }

    public static void assertFalse(boolean bool, IError error) {
        assertFalse(bool, error, null);
    }

    public static void assertFalse(boolean bool, IError error, Object[] params) {
        assertTrue(!bool, error, params);
    }

    public static void assertFalse(boolean bool, Integer code, String message) {
        assertFalse(bool, code, message, null);
    }

    public static void assertFalse(boolean bool, Integer code, String message, Object... params) {
        assertFalse(bool, ErrorInfo.of(code, message), params);
    }

    /**
     * Opt = Optional.Empty抛出异常
     *
     * @param opt
     * @param error 异常枚举
     */
    private static void orThrow(Optional<?> opt, IError error, Object... params) {
        opt.orElseThrow(() -> {
            String errMsg = handleI18n(error, params);
            log.error("数据校验异常. Error:{}, message: {}", error.getCode(), errMsg);
            return new BizException(error.getCode(), errMsg);
        });
    }

    private static String handleI18n(IError error, Object... params) {
        String msg = error.getMessage();

        // 为空不处理
        if (StringUtil.isBlank(msg)) {
            return msg;
        }

        try {
            return String.format(msg, params);
        } catch (NoSuchMessageException e) {
            log.debug("[{}]国际化失败，请检查国际化配置。。。", msg);
        }

        return msg;
    }

    @Getter
    @AllArgsConstructor
    enum UnknownError implements IError {
        DATA_ERROR(UNKNOWN, "数据异常, 请检查数据.");

        private Integer code;

        private String message;
    }
}
