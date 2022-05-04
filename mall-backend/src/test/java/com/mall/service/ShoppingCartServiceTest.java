package com.mall.service;

import com.mall.model.domain.ShoppingCart;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShoppingCartServiceTest {

    @Resource
    private ShoppingCartService shoppingCartService;

    @Test
    void addTest(){
        String productId = "1";
        for (int i = 9; i < 109; i++) {
            shoppingCartService.addShoppingCart(String.valueOf(i),productId);
        }
    }

    @Test
    void selectTest(){
        String userId = "1";
        List<ShoppingCart> shoppingCart = shoppingCartService.getShoppingCart(userId);
        System.out.println(shoppingCart==null);
        shoppingCart.forEach(System.out::println);
    }

    @Test
    void deleteTest(){
        Boolean aBoolean = shoppingCartService.deleteShoppingCart("2", "10");
        assertEquals(true,aBoolean);
    }

    @Test
    void updateTest(){
        Boolean aBoolean = shoppingCartService.updateShoppingCart("2", "18", 2);
        assertEquals(true,aBoolean);
    }

}