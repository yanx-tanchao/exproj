package cn.exp.proj.common.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseVo {
    protected Long id;

    protected Long tenantId;

    protected Boolean deleted;

    protected String remark;

    protected Long creator;

    protected String createName;

    protected LocalDateTime createTime;

    protected Long updater;

    protected String updateName;

    protected LocalDateTime updateTime;
}
