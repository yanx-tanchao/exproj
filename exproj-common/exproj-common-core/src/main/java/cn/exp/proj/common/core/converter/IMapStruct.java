package cn.exp.proj.common.core.converter;

import cn.exp.proj.common.core.BaseEntity;

import java.util.Objects;

public interface IMapStruct<E, V> {
    E toEntity(V vo);

    V toVo(E entity);

    V newVo(V vo);

    E newEntity(E entity);

    default void fillBaseField(BaseEntity source, BaseEntity target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            return;
        }
        if (source.getTenantId() != null) {
            target.setTenantId(source.getTenantId());
        }
    }

    default void fillUserInfo(BaseEntity source, BaseEntity target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            return;
        }
        if (source.getCreateName() != null) {
            target.setCreateName(source.getCreateName());
        }
        if (source.getCreator() != null) {
            target.setCreator(source.getCreator());
        }
        if (source.getUpdateName() != null) {
            target.setUpdateName(source.getUpdateName());
        }
        if (source.getUpdater() != null) {
            target.setUpdater(source.getUpdater());
        }
    }
}
