package com.mall.service.impl;

import com.mall.model.domain.Orders;
import com.mall.service.OrdersService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrdersServiceImplTest {

    @Resource
    private OrdersService ordersService;

    @Test
    void addTest(){
        Orders order = new Orders();
        order.setOrderId(2L);
        order.setUserId(4);
        order.setProductId(20);
        order.setProductNum(3);
        order.setProductPrice(39.0);
        ordersService.save(order);
    }

    @Test
    void getTest(){
        ordersService.getOrders("1").forEach(System.out::println);
        System.out.println(ordersService.getOrders("1").size());
    }

    @Test
    void timeTest(){
        String userId = "4";
        Long orderId = Long.valueOf(new Date().getTime() + userId);
        System.out.println(orderId);
    }

}