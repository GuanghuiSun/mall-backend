package com.mall.service;

import com.mall.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser(){
        User user = new User();
        user.setUsername("test");
        user.setAvatarUrl("https://account.bilibili.com/account/face/upload?spm_id_from=333.999.0.0");
        user.setUserPassword("123456");
        user.setPhone("19910762339");
        user.setEmail("1849920218@qq.com");
        boolean save = userService.save(user);
        System.out.println(user.getUserId());
        assertTrue(save);
    }


    @Test
    void userRegister() {
        String username = "testasdaa";
        String password = "";
        String checkPassword = "1234567";
        long l = userService.userRegister(username, password, checkPassword);
        Assertions.assertEquals(-1,l);
        username = "te";
        password = "1234567";
        long l2 = userService.userRegister(username,password,checkPassword);
        Assertions.assertEquals(-1,l2);
        username = "*&((&%$%^%^&";
        long l3 = userService.userRegister(username,password,checkPassword);
        Assertions.assertEquals(-1,l3);
        username = "test";
        long l4 = userService.userRegister(username,password,checkPassword);
        Assertions.assertEquals(-1,l4);
        username = "woshiceshi";
        password = "123";
        long l5 = userService.userRegister(username,password,checkPassword);
        Assertions.assertEquals(-1,l5);
        password = "1234567";
        long result = userService.userRegister(username,password,checkPassword);
        assertTrue(result > 0 );
    }
}