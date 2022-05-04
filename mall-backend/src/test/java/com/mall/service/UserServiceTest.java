package com.mall.service;

import cn.hutool.core.io.file.FileWrapper;
import com.mall.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    void testAddUser() throws IOException {
        Map<Integer,String> map = new HashMap<>();
        FileWriter fileWriter = new FileWriter(new File("C:\\Users\\huawei\\Desktop\\token.txt"));
        for (int i = 0; i < 100; i++) {
            String name = "batchUser"+i;
            String password = "user123456";
            String token = userService.userLogin(name, password);
//            map.put(9+i,token);
            fileWriter.write(token);
            fileWriter.write("\n");
        }
//        fileWriter.write(String.valueOf(9) + " " + "asdada");
//        fileWriter.write("\n");
//        fileWriter.write(String.valueOf(9) + " " + "asdada");
        fileWriter.close();
    }


    @Test
    void userRegister() {
//        String username = "testasdaa";
//        String password = "";
//        String checkPassword = "1234567";
//        long l = userService.userRegister(username, password, checkPassword);
//        Assertions.assertEquals(-1,l);
//        username = "te";
//        password = "1234567";
//        long l2 = userService.userRegister(username,password,checkPassword);
//        Assertions.assertEquals(-1,l2);
//        username = "*&((&%$%^%^&";
//        long l3 = userService.userRegister(username,password,checkPassword);
//        Assertions.assertEquals(-1,l3);
//        username = "test";
//        long l4 = userService.userRegister(username,password,checkPassword);
//        Assertions.assertEquals(-1,l4);
//        username = "woshiceshi";
//        password = "123";
//        long l5 = userService.userRegister(username,password,checkPassword);
//        Assertions.assertEquals(-1,l5);
//        password = "1234567";
//        long result = userService.userRegister(username,password,checkPassword);
//        assertTrue(result > 0 );
    }
}