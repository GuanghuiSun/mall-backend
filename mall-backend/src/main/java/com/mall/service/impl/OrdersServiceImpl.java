package com.mall.service.impl;
import cn.hutool.core.util.BooleanUtil;

import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.model.domain.Orders;
import com.mall.model.domain.ShoppingCart;
import com.mall.service.OrdersService;
import com.mall.mapper.OrdersMapper;
import com.mall.service.ShoppingCartService;
import com.mall.utils.RedisIdWorker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.mall.constant.RedisConstant.INCR_ORDER_KEY;

/**
* @author sgh
*/
@Service
@Transactional
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private ShoppingCartService shoppingCartService;

    @Override
    public List<List<Orders>> getOrders(String userId) {
        List<Orders> orders = ordersMapper.getOrders(userId);
        if(orders == null) {
            return Collections.emptyList();
        }
        //将订单按订单编号进行分组
        List<List<Orders>> result = new ArrayList<>();
        Map<Long,List<Orders>> map = new HashMap<>();
        for(Orders order : orders){
            List<Orders> value;
            if(map.containsKey(order.getOrderId())){
                value = map.get(order.getOrderId());

            }else{
                value = new ArrayList<>();
            }
            value.add(order);
            map.put(order.getOrderId(),value);
        }
        List<Long> ids = new ArrayList<>(map.keySet());
        ids.sort((o1, o2) -> o1 - o2 > 0 ? -1 : 1);
        for(Long orderId : ids){
            result.add(map.get(orderId));
        }
        return result;
    }

    @Override
    public Boolean addOrders(String userId, ShoppingCart[] shoppingCarts) {
        //由redisID生成器生成唯一的订单id
        long orderId = redisIdWorker.nextId(INCR_ORDER_KEY);
        for(ShoppingCart shoppingCart : shoppingCarts){
            Orders orders = new Orders();
            orders.setOrderId(orderId);
            orders.setUserId(Integer.parseInt(userId));
            orders.setProductId(shoppingCart.getProductId());
            orders.setProductNum(shoppingCart.getNum());
            orders.setProductPrice(shoppingCart.getProduct().getProductSellingPrice());
            //将订单插入到数据库中
            boolean save = this.save(orders);
            if(!save){
                return false;
            }
        }
        //更改购物车中的相关数据
        for(ShoppingCart shoppingCart : shoppingCarts){
            Boolean result = shoppingCartService.updateProductStatus(String.valueOf(shoppingCart.getUserId()), String.valueOf(shoppingCart.getProductId()));
            if(Boolean.FALSE.equals(result)){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<List<Orders>> getAllOrders() {
        List<Orders> orders = list();
        if(orders == null) {
            return Collections.emptyList();
        }
        //将订单按订单编号进行分组
        List<List<Orders>> result = new ArrayList<>();
        Map<Long,List<Orders>> map = new HashMap<>();
        for(Orders order : orders){
            List<Orders> value;
            if(map.containsKey(order.getOrderId())){
                value = map.get(order.getOrderId());

            }else{
                value = new ArrayList<>();
            }
            value.add(order);
            map.put(order.getOrderId(),value);
        }
        List<Long> ids = new ArrayList<>(map.keySet());
        ids.sort((o1, o2) -> o1 - o2 > 0 ? -1 : 1);
        for(Long orderId : ids){
            result.add(map.get(orderId));
        }
        return result;
    }
}




