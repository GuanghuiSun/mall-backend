package com.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall.model.domain.Product;
import com.mall.model.domain.ProductPicture;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Resource
    private ProductService productService;

    @Resource
    private ProductPictureService productPictureService;

    @Test
    void getTest(){
//        IPage<Product> productByCategory = productService.getProductByCategory(1, 1, 7);
        IPage<Product> productByCategory = productService.getALlProduct(1,7);
        productByCategory.getRecords().forEach(System.out::println);
    }

    @Test
    void searchTest(){
        IPage<Product> product = productService.getProductBySearch("充电宝", 1, 7);
        product.getRecords().forEach(System.out::println);

    }

    @Test
    void getPictureTest(){
        List<ProductPicture> detailsPicture = productPictureService.getDetailsPicture("4");
        detailsPicture.forEach(System.out::println);
    }

}