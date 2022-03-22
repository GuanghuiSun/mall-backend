package com.mall.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @author sgh
 * @TableName product_picture
 */
@TableName(value ="product_picture")
@Data
public class ProductPicture implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品id
     */
    @TableField(value = "product_id")
    private Integer productId;

    /**
     * 商品图片位置
     */
    @TableField(value = "product_picture_url")
    private String productPictureUrl;

    /**
     * 商品图片介绍
     */
    @TableField(value = "intro")
    private String intro;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}