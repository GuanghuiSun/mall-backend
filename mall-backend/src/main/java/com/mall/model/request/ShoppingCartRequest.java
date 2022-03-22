package com.mall.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 更改购物车请求
 */
@Data
public class ShoppingCartRequest implements Serializable {
    private static final long serialVersionUID = 6235880623006257066L;
    private String userId;
    private String productId;
    private Integer num;
}
