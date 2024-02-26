package cn.exp.proj.common.core.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统基础常量
 * 主要包含业务含义一样的常量
 * 以及各模块共用的常量
 */
public interface IConstant {

    @Getter
    @AllArgsConstructor
    public enum ResultEnum {
        SUCCESS("1", "成功"),

        FAIL("2", "失败");

        private String code;

        private String desc;
    }
}
