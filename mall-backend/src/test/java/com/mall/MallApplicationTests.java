package com.mall;

import com.mall.job.HotProductJob;
import com.mall.model.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MallApplicationTests {

    @Resource
    private HotProductJob hotProductJob;

    @Test
    void contextLoads() {
    }

    @Test
    void testMD5(){
        Product product = new Product();
        product.setProductName("asd");
        System.out.println(product);
    }

    @Test
    void testSchedule(){
//        hotProductJob.refreshRedisTask();
    }
}
