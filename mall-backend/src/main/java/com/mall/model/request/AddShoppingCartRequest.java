package com.mall.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 加入购物车请求
 */
@Data
public class AddShoppingCartRequest implements Serializable {
    private static final long serialVersionUID = 8671124915566600405L;
    private Integer userId;
    private Integer productId;
}
