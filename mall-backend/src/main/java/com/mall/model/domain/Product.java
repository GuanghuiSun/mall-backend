package com.mall.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 
 * @TableName product
 */
@TableName(value ="product")
@Data
public class Product implements Serializable {
    /**
     * 产品id
     */
    @TableId(value = "product_id", type = IdType.AUTO)
    private Integer productId;

    /**
     * 产品名称
     */
    @TableField(value = "product_name")
    private String productName;

    /**
     * 种类id
     */
    @TableField(value = "category_id")
    private Integer categoryId;

    /**
     * 产品标题描述
     */
    @TableField(value = "product_title")
    private String productTitle;

    /**
     * 产品介绍
     */
    @TableField(value = "product_intro")
    private String productIntro;

    /**
     * 产品图片
     */
    @TableField(value = "product_picture")
    private String productPicture;

    /**
     * 产品展示图片组
     */
    @TableField(exist = false)
    private List<String> pictures;

    /**
     * 产品原价
     */
    @TableField(value = "product_price")
    private Double productPrice;

    /**
     * 产品售卖价格

     */
    @TableField(value = "product_selling_price")
    private Double productSellingPrice;

    /**
     * 产品总数
     */
    @TableField(value = "product_num")
    private Integer productNum;

    /**
     * 产品销量
     */
    @TableField(value = "product_sales")
    private Integer productSales;

    /**
     * 产品是否存在
     */
    @TableField(value = "is_deleted")
    private Byte isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}