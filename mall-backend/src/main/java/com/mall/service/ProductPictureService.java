package com.mall.service;

import com.mall.model.domain.ProductPicture;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author huawei
* @description 针对表【product_picture】的数据库操作Service
* @createDate 2022-03-21 18:00:00
*/
public interface ProductPictureService extends IService<ProductPicture> {

    /**
     * 获取商品的所有图片
     * @param productId 商品id
     * @return
     */
    List<ProductPicture> getDetailsPicture(String productId);
}
