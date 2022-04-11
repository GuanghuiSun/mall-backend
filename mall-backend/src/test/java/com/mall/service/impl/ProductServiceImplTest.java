package com.mall.service.impl;

import com.mall.model.domain.Product;
import com.mall.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceImplTest {

    @Resource
    private ProductService productService;


    @Test
    void listTest(){
        String[] names = {"保护套", "保护膜", "充电器", "充电宝"};
        List<Product> hotProduct = productService.getHotProduct(names);
        hotProduct.forEach(System.out::println);
    }

    @Test
    void orderProduct() {
        productService.orderProduct(2,1);
    }
}