package com.mall.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除收藏中的商品
 */
@Data
public class CollectRequest implements Serializable {
    private static final long serialVersionUID = 6522487157157986725L;
    private String userId;
    private String productId;
}
