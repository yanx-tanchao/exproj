package cn.exp.proj.common.core.wrapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Optional;

/**
 * 判断包装类
 * 用于逻辑比较分散的各种判断
 * 入参等于null则 默认为 False
 */
@NoArgsConstructor
@AllArgsConstructor
public class BooleanWrapper {
    private Boolean condtion;

    public static BooleanWrapper empty() {
        return new BooleanWrapper();
    }

    public static BooleanWrapper of(Boolean condtion) {
        return new BooleanWrapper(ifNull(condtion));
    }

    public static BooleanWrapper ofTrue(Boolean condtion) {
        return new BooleanWrapper(Optional.ofNullable(condtion).orElse(Boolean.TRUE));
    }

    public BooleanWrapper and(Boolean andCond) {
        if (Objects.isNull(condtion)) {
            this.condtion = ifNull(andCond);
            return this;
        }
        this.condtion = condtion && ifNull(andCond);
        return this;
    }

    public BooleanWrapper and(BooleanWrapper wrapper) {
        Boolean cond = Optional.ofNullable(wrapper).map(BooleanWrapper::eval).orElse(Boolean.FALSE);
        return and(cond);
    }

    public BooleanWrapper or(Boolean orCond) {
        if (Objects.isNull(condtion)) {
            this.condtion = ifNull(orCond);
            return this;
        }
        this.condtion = condtion || ifNull(orCond);
        return this;
    }

    public BooleanWrapper or(BooleanWrapper wrapper) {
        Boolean cond = Optional.ofNullable(wrapper).map(BooleanWrapper::eval).orElse(Boolean.FALSE);
        return or(cond);
    }

    /**
     * 默认返回 false
     *
     * @return condtion
     */
    public boolean eval() {
        return ifNull(condtion);
    }

    static Boolean ifNull(Boolean condtion) {
        return Optional.ofNullable(condtion).orElse(Boolean.FALSE);
    }
}
