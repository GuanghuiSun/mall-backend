package com.mall.service;

import com.mall.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author huawei
* @description 针对表【user】的数据库操作Service
* @createDate 2022-03-20 10:48:34
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param checkPassword 校验密码
     * @return
     */
    long userRegister(String username, String password, String checkPassword);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    User userLogin(String username, String password, HttpServletRequest request);

    /**
     * 查询用户是否存在
     * @param username 用户名
     * @return
     */
    long checkUsername(String username);
}
