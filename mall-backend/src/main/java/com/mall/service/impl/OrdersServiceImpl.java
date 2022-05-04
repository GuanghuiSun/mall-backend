package com.mall.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.*;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.config.RabbitMQConfig;
import com.mall.exception.BusinessException;
import com.mall.model.domain.OrderMessageDTO;
import com.mall.model.domain.Orders;
import com.mall.model.domain.Product;
import com.mall.model.domain.ShoppingCart;
import com.mall.service.OrdersService;
import com.mall.mapper.OrdersMapper;
import com.mall.service.ProductService;
import com.mall.service.ShoppingCartService;
import com.mall.utils.RedisIdWorker;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.mall.base.ErrorCode.REQUEST_SERVICE_ERROR;
import static com.mall.constant.MessageConstant.*;
import static com.mall.constant.RedisConstant.INCR_ORDER_KEY;
import static com.mall.constant.RedisConstant.INVENTORY_KEY;

/**
 * @author sgh
 */
@Service
@Transactional
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
        implements OrdersService {

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ShoppingCartService shoppingCartService;

    @Resource
    private ProductService productService;

    private static final DefaultRedisScript<Long> CHANGE_INVENTORY;

    static {
        CHANGE_INVENTORY = new DefaultRedisScript<>();
        CHANGE_INVENTORY.setLocation(new ClassPathResource("scripts/changeInventory.lua"));
        CHANGE_INVENTORY.setResultType(Long.class);//返回结果类型
    }

    @Override
    public List<List<Orders>> getOrders(String userId) {
        List<Orders> orders = ordersMapper.getOrders(userId);
        if (orders == null) {
            return Collections.emptyList();
        }
        //将订单按订单编号进行分组
        List<List<Orders>> result = new ArrayList<>();
        Map<Long, List<Orders>> map = new HashMap<>();
        for (Orders order : orders) {
            List<Orders> value;
            if (map.containsKey(order.getOrderId())) {
                value = map.get(order.getOrderId());

            } else {
                value = new ArrayList<>();
            }
            value.add(order);
            map.put(order.getOrderId(), value);
        }
        List<Long> ids = new ArrayList<>(map.keySet());
        ids.sort((o1, o2) -> o1 - o2 > 0 ? -1 : 1);
        for (Long orderId : ids) {
            result.add(map.get(orderId));
        }
        return result;
    }

    @Override
    public Boolean addOrders(String userId, ShoppingCart[] shoppingCarts) {
        for (ShoppingCart shoppingCart : shoppingCarts) {
            Integer productId = shoppingCart.getProductId();
            Integer num = shoppingCart.getNum();
            String key = INVENTORY_KEY + ":" + productId;
            Long changed = stringRedisTemplate.execute(CHANGE_INVENTORY,
                    Collections.singletonList(key), String.valueOf(num));
            if(changed == 0){
                //库存不足
                throw new BusinessException(REQUEST_SERVICE_ERROR,INVENTORY_SHORTAGE_ERROR);
            }
//            封装订单消息
            OrderMessageDTO orderMessageDTO = new OrderMessageDTO();
            orderMessageDTO.setOrderId(redisIdWorker.nextId(INCR_ORDER_KEY));
            orderMessageDTO.setUserId(Integer.valueOf(userId));
            orderMessageDTO.setProductId(productId);
            orderMessageDTO.setProductPrice(shoppingCart.getProduct().getProductSellingPrice());
            orderMessageDTO.setNum(num);
            String json = JSONUtil.toJsonStr(orderMessageDTO);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message message = new Message(json.getBytes(StandardCharsets.UTF_8), messageProperties);
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(String.valueOf(orderMessageDTO.getOrderId()));//设置为订单ID，全局唯一
            ReturnedMessage returnedMessage = new ReturnedMessage(message, 200, "用户创建订单消息",
                    RabbitMQConfig.ORDER_EXCHANGE, RabbitMQConfig.ORDER_ROUTING_KEY);
            correlationData.setReturned(returnedMessage);//设置不可路由捕获消息
            //发送消息
            rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_ROUTING_KEY,
                    message,correlationData);
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean handleOrder(OrderMessageDTO orderMessageDTO) {

        //数据库添加订单
        Orders order = new Orders();
        order.setOrderId(orderMessageDTO.getOrderId());
        Integer userId = orderMessageDTO.getUserId();
        order.setUserId(userId);
        Integer productId = orderMessageDTO.getProductId();
        order.setProductId(productId);
        Integer num = orderMessageDTO.getNum();
        order.setProductNum(num);
        order.setProductPrice(orderMessageDTO.getProductPrice());
        boolean save = this.save(order);
        if(!save){
            throw new BusinessException(REQUEST_SERVICE_ERROR,CREATE_ORDER_ERROR);
        }
        //修改商品销售额
        LambdaUpdateWrapper<Product> wrapper = new LambdaUpdateWrapper<>();
        Product product = productService.getById(productId);
        wrapper.eq(Product::getProductId,productId).le(Product::getProductSales,product.getProductNum()).set(Product::getProductSales,product.getProductSales()+num);
        boolean update = productService.update(wrapper);
        if(!update){
            throw new BusinessException(REQUEST_SERVICE_ERROR,ORDER_FAIL);
        }
        //删除购物车
//        Boolean success = shoppingCartService.deleteShoppingCart(String.valueOf(userId), String.valueOf(productId));
//        if(Boolean.FALSE.equals(success)){
//            throw new BusinessException(REQUEST_SERVICE_ERROR,ORDER_FAIL);
//        }
        return Boolean.TRUE;
    }

    @Override
    public List<List<Orders>> getAllOrders() {
        List<Orders> orders = list();
        if (orders == null) {
            return Collections.emptyList();
        }
        //将订单按订单编号进行分组
        List<List<Orders>> result = new ArrayList<>();
        Map<Long, List<Orders>> map = new HashMap<>();
        for (Orders order : orders) {
            List<Orders> value;
            if (map.containsKey(order.getOrderId())) {
                value = map.get(order.getOrderId());

            } else {
                value = new ArrayList<>();
            }
            value.add(order);
            map.put(order.getOrderId(), value);
        }
        List<Long> ids = new ArrayList<>(map.keySet());
        ids.sort((o1, o2) -> o1 - o2 > 0 ? -1 : 1);
        for (Long orderId : ids) {
            result.add(map.get(orderId));
        }
        return result;
    }
}




