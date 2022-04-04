package com.mall.model.request;

import lombok.Data;

/**
 * 添加商品图片请求体
 *
 * @author sgh
 */
@Data
public class ProductPictureRequest {
    private String[] pictures;
    private Integer productId;
    private String pictureIntro;
}
