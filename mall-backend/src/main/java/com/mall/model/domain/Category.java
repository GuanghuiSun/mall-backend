package com.mall.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 分类
 * @TableName category
 */
@TableName(value ="category")
@Data
public class Category implements Serializable {
    /**
     * 种类id
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;

    /**
     * 种类名称
     */
    @TableField(value = "category_name")
    private String categoryName;

    /**
     * 逻辑删除
     */
    @TableField(value = "is_deleted")
    private Byte isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}