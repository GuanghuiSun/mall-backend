package com.mall.controller;

import com.mall.base.BaseResponse;
import com.mall.base.ResultUtils;
import com.mall.constant.MessageConstant;
import com.mall.model.domain.Orders;
import com.mall.model.domain.ShoppingCart;
import com.mall.model.request.OrderRequest;
import com.mall.service.OrdersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrdersService ordersService;

    /**
     * 获取该用户的所有订单信息
     *
     * @param orderRequest 订单请求体
     * @return
     */
    @PostMapping("/getOrder")
    public BaseResponse<List<List<Orders>>> getOrder(@RequestBody OrderRequest orderRequest) {
        if (orderRequest == null) {
            return ResultUtils.error(null, MessageConstant.REQUEST_FAIL);
        }
        String userId = orderRequest.getUserId();
        if (StringUtils.isAnyBlank(userId)) {
            return ResultUtils.error(null, MessageConstant.REQUEST_FAIL);
        }
        List<List<Orders>> orders = ordersService.getOrders(userId);
        if (orders == null) {
            return ResultUtils.error(null, MessageConstant.SELECT_FAIL);
        }
        if(orders.isEmpty()){
            return ResultUtils.success("002",orders, MessageConstant.ORDER_EMPTY);
        }
        return ResultUtils.success(orders, MessageConstant.SELECT_SUCCESS);

    }

    /**
     * 添加订单
     * @param orderRequest 订单请求体
     * @return
     */
    @PostMapping("/addOrder")
    public BaseResponse<Boolean> addOrders(@RequestBody OrderRequest orderRequest){
        if (orderRequest == null) {
            return ResultUtils.error(null, MessageConstant.REQUEST_FAIL);
        }
        String userId = orderRequest.getUserId();
        ShoppingCart[] shoppingCart = orderRequest.getShoppingCart();
        System.out.println(userId);
        for(ShoppingCart shoppingCart1 : shoppingCart){
            System.out.println(shoppingCart1);
        }
        if (StringUtils.isAnyBlank(userId) || shoppingCart == null || shoppingCart.length == 0) {
            return ResultUtils.error(null, MessageConstant.REQUEST_FAIL);
        }
        Boolean result = ordersService.addOrders(userId, shoppingCart);
        if(Boolean.TRUE.equals(result)){
            return ResultUtils.success(true,MessageConstant.ORDER_SUCCESS);
        } else {
            return ResultUtils.error(false,MessageConstant.ORDER_FAIL);
        }

    }


}
