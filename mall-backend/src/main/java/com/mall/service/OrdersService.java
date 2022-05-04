package com.mall.service;

import com.mall.model.domain.OrderMessageDTO;
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
     *
     * @param userId 用户id
     * @return 订单列表
     */
    List<List<Orders>> getOrders(String userId);

    /**
     * 增加订单信息
     *
     * @param userId       用户id
     * @param shoppingCart 购物车
     * @return 是否成功
     */
    Boolean addOrders(String userId, ShoppingCart[] shoppingCart);

    /**
     * 处理订单消息
     * 创建订单 修改商品销售额 删除购物车
     *
     * @param orderMessageDTO 订单消息
     * @return 是否成功
     */
    Boolean handleOrder(OrderMessageDTO orderMessageDTO);

    /**
     * 获取所有订单
     *
     * @return 订单列表
     */
    List<List<Orders>> getAllOrders();


}
