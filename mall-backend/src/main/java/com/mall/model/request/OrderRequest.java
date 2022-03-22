package com.mall.model.request;

import com.mall.model.domain.ShoppingCart;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单请求体
 */
@Data
public class OrderRequest implements Serializable {
    private static final long serialVersionUID = -4301709871806892495L;
    private String userId;
    private ShoppingCart[] shoppingCart;
}
