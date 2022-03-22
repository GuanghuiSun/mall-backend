package com.mall.service;

import com.mall.model.domain.Collect;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author sgh
* @createDate 2022-03-19 22:47:30
*/
public interface CollectService extends IService<Collect> {

    /**
     * 获取收藏列表
     * @param userId 用户id
     * @return
     */
    List<Collect> getCollect(String userId);

    /**
     * 删除收藏列表中的商品
     * @param productId 商品id
     * @return
     */
    Boolean deletedCollect(String userId, String productId);

    /**
     * 添加商品到收藏列表
     * @param userId 用户id
     * @param productId 商品id
     * @return
     */
    Integer addCollect(String userId, String productId);
}
