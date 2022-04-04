package com.mall.service;

import com.mall.model.domain.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.model.domain.ShoppingCart;

import java.util.List;

/**
* @author sgh
* @createDate 2022-03-19 21:49:09
*/
public interface OrdersService extends IService<Orders> {

    /**
     * 获取指定用户订单信息
     * @param userId 用户id
     * @return
     */
    List<List<Orders>> getOrders(String userId);

    /**
     * 增加订单信息
     * @param userId 用户id
     * @param shoppingCart 购物车
     * @return
     */
    Boolean addOrders(String userId, ShoppingCart[] shoppingCart);

    /**
     * 获取所有订单
     * @return
     */
    List<List<Orders>> getAllOrders();


}
