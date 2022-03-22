package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.model.domain.User;
import com.mall.service.UserService;
import com.mall.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author sgh
* @createDate 2022-03-20 10:48:34
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    //盐值
    private static final String SALT = "yan";
    //用户登录态键
    private static final String USER_LOGIN_STATE = "userLoginState";

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String username, String password, String checkPassword) {
        //1.校验非空
        if (StringUtils.isAnyBlank(username, password, checkPassword)) {
            return -1;
        }
        //1.1校验用户名
        if (username.length() < 4) {
            return -1;
        }
        String valuePattern = "^[a-z_\\d]{4,20}$";
        Matcher matcher = Pattern.compile(valuePattern).matcher(username);
        if (!matcher.find()) {
            return -1;
        }
        //1.2校验密码
        if (password.length() < 6) {
            return -1;
        }
        //1.3校验两次密码是否一样
        if (!password.equals(checkPassword)) {
            return -1;
        }
        //2.账户不能重复
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        long count = this.count(wrapper);
        if (count > 0) {
            return -1;
        }
        //4.对密码进行加密
        String handledPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
        //5.将用户数据插入到数据库
        User user = new User();
        user.setUsername(username);
        user.setUserPassword(handledPassword);
        boolean save = this.save(user);
        if(!save){
            return -1;
        }
        return user.getUserId();
    }

    @Override
    public User userLogin(String username, String password, HttpServletRequest request) {
        //1.校验非空
        if (StringUtils.isAnyBlank(username, password)) {
            return null;
        }
        //1.1校验用户名
        if (username.length() < 4) {
            return null;
        }
        String valuePattern = "^[a-z_\\d]{4,20}$";
        Matcher matcher = Pattern.compile(valuePattern).matcher(username);
        if (!matcher.find()) {
            return null;
        }
        //1.2校验密码
        if (password.length() < 6) {
            return null;
        }
        //校验用户名和密码
        String handledPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username).eq(User::getUserPassword,handledPassword);
        User user = userMapper.selectOne(wrapper);
        //用户不存在
        if(user == null){
            log.info("user login failed, username can not match password");
            return null;
        }
        //对用户脱敏
        User safetyUser = new User();
        safetyUser.setUserId(user.getUserId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setGender(user.getGender());
        safetyUser.setIsValid((byte)0);
        safetyUser.setCreateTime(new Date());
        //记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    @Override
    public long checkUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username);
        long count = this.count(wrapper);
        if(count > 0){
            return -1;
        }
        return 1;
    }
}




