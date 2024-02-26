package cn.exp.proj.common.core;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class BaseEntity<T extends Model<?>> extends Model<T> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    protected Long id;

    /**
     * 租户ID
     */
    protected Long tenantId;

    /**
     * 删除状态：是否删除，0未删除，1已删除
     */
    @TableLogic
    protected Boolean deleted;

    /**
     * 备注
     */
    protected String remark;

    @TableField(fill = FieldFill.INSERT)
    protected Long creator;

    @TableField(fill = FieldFill.INSERT)
    protected String createName;

    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Long updater;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected String updateName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;
}
