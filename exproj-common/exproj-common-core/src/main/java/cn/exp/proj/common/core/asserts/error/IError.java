package cn.exp.proj.common.core.asserts.error;

/**
 * 异常信息接口
 * 使用断言工具抛出异常时可以使用创建枚举的方式，实现该接口
 */
public interface IError {
    static final Integer UNKNOWN = 10000;

    static final Integer COMMON = 210000;

    /**
     * 异常错误码
     * 各模块异常起始值配置在该接口中
     *
     * @return 异常码
     */
    Integer getCode();

    /**
     * 错误信息
     * 可使用国际化key值
     *
     * @return 错误信息或错误信息的国际化key
     */
    String getMessage();
}
