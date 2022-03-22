package com.mall.service;

import com.mall.model.domain.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author sgh
* @createDate 2022-03-19 20:47:40
*/
public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 查询购物车
     * @param userId 用户id
     * @return
     */
    List<ShoppingCart> getShoppingCart(String userId);

    /**
     * 删除购物车中的商品
     * @param userId 用户id
     * @param productId 商品id
     * @return
     */
    Boolean deleteShoppingCart(String userId,String productId);

    /**
     * 更新购物车
     * @param userId 用户id
     * @param productId 商品id
     * @param num 数量
     * @return
     */
    Boolean updateShoppingCart(String userId, String productId, Integer num);

    /**
     * 添加购物车
     * @param userId 用户id
     * @param productId 商品id
     * @return
     */
    Map<ShoppingCart, Integer> addShoppingCart(String userId, String productId);
}
