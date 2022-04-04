package com.mall.controller;

import com.mall.base.BaseResponse;
import com.mall.base.ResultUtils;
import com.mall.constant.MessageConstant;
import com.mall.model.domain.Orders;
import com.mall.model.domain.ShoppingCart;
import com.mall.model.request.OrderRequest;
import com.mall.service.OrdersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.mall.constant.MessageConstant.SELECT_SUCCESS;

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
        return ResultUtils.success(orders, SELECT_SUCCESS);
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

    /**
     * 获取所有订单
     * @return
     */
    @GetMapping()
    public BaseResponse<List<List<Orders>>> getAllOrders(){
        List<List<Orders>> result = ordersService.getAllOrders();
        return ResultUtils.success(result,SELECT_SUCCESS);
    }

}
