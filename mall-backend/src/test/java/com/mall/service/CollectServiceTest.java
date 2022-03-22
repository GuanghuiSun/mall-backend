package com.mall.service;

import com.mall.model.domain.Collect;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CollectServiceTest {

    @Resource
    private CollectService collectService;

    @Test
    void addTest(){
        Collect c = new Collect();
        c.setUserId(4);
        c.setProductId(30);
    collectService.save(c);
    }

    @Test
    void listTest(){
        List<Collect> collect = collectService.getCollect("4");
        collect.forEach(System.out::println);

    }
}