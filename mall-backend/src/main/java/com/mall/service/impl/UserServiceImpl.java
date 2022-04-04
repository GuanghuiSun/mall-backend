package com.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.base.BaseResponse;
import com.mall.base.ResultUtils;
import com.mall.model.domain.ShoppingCart;
import com.mall.model.domain.User;
import com.mall.model.domain.UserDTO;
import com.mall.service.ShoppingCartService;
import com.mall.service.UserService;
import com.mall.mapper.UserMapper;
import com.mall.utils.UserHolder;
import io.netty.util.ResourceLeakDetector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.mall.constant.MessageConstant.*;
import static com.mall.constant.RedisConstant.LOGIN_USER_TTL;
import static com.mall.constant.RedisConstant.USER_LOGIN_STATE;

/**
 * @author sgh
 * @createDate 2022-03-20 10:48:34
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    //盐值
    private static final String SALT = "yan";

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ShoppingCartService shoppingCartService;

    @Override
    public BaseResponse<Long> userRegister(String username, String password, String checkPassword) {
        //1.校验非空
        if (StringUtils.isAnyBlank(username, password, checkPassword)) {
            return ResultUtils.error(null, EMPTY_REGISTER_FAIL);
        }
        //1.1校验用户名
        if (username.length() < 4) {
            return ResultUtils.error(null, LENGTH_REGISTER_FAIL);
        }
        String valuePattern = "^[a-z_\\d]{4,20}$";
        Matcher matcher = Pattern.compile(valuePattern).matcher(username);
        if (!matcher.find()) {
            return ResultUtils.error(null, PATTERN_REGISTER_FAIL);
        }
        //1.2校验密码
        if (password.length() < 6) {
            return ResultUtils.error(null, LENGTH_REGISTER_FAIL);
        }
        //1.3校验两次密码是否一样
        if (!password.equals(checkPassword)) {
            return ResultUtils.error(null, CHECK_REGISTER_FAIL);
        }
        //2.账户不能重复
        if (checkUsername(username) < 0) {
            return ResultUtils.error(null, REPEAT_USERNAME_FAIL);
        }
        //4.对密码进行加密
        String handledPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
        //5.将用户数据插入到数据库
        User user = new User();
        user.setUsername(username);
        user.setUserPassword(handledPassword);
        boolean save = this.save(user);
        if (!save) {
            return ResultUtils.error(null, ERROR_REGISTER_FAIL);
        }
        return ResultUtils.success(Long.valueOf(user.getUserId()), REGISTER_SUCCESS);
    }

    @Override
    public BaseResponse<String> userLogin(String username, String password, HttpServletRequest request) {
        //1.校验非空
        if (StringUtils.isAnyBlank(username, password)) {
            return ResultUtils.error(null, EMPTY_REGISTER_FAIL);
        }
        //1.1校验用户名
        if (username.length() < 4) {
            return ResultUtils.error(null, LENGTH_REGISTER_FAIL);
        }
        String valuePattern = "^[a-z_\\d]{4,20}$";
        Matcher matcher = Pattern.compile(valuePattern).matcher(username);
        if (!matcher.find()) {
            return ResultUtils.error(null, PATTERN_REGISTER_FAIL);
        }
        //1.2校验密码
        if (password.length() < 6) {
            return ResultUtils.error(null, LENGTH_REGISTER_FAIL);
        }
        LambdaQueryWrapper<User> validWrapper = new LambdaQueryWrapper<>();
        validWrapper.eq(User::getUsername, username).eq(User::getIsValid, 0);
        Long count = userMapper.selectCount(validWrapper);
        if (count == 0) {
            return ResultUtils.error(null, USER_NOT_VALID);
        }
        //校验用户名和密码
        String handledPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username).eq(User::getUserPassword, handledPassword);
        User user = userMapper.selectOne(wrapper);
        //用户不存在
        if (user == null) {
            log.info("user login failed, username can not match password");
            return ResultUtils.error(null, CHECK_PASSWORD_FAIL);
        }
        //对用户脱敏
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        //记录用户登录态
        String token = UUID.randomUUID().toString();
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create().setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        String tokenKey = USER_LOGIN_STATE + ":" + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        //设置有效期
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
        //保存用户到UserHolder
        UserHolder.saveUser(userDTO);
        return ResultUtils.success(token, LOGIN_SUCCESS);
    }

    @Override
    public long checkUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        long count = this.count(wrapper);
        if (count > 0) {
            return -1;
        }
        return 1;
    }

    @Override
    public BaseResponse<List<UserDTO>> getAllUsers() {
        List<User> users = list();
        if (users == null) {
            return ResultUtils.fail(SELECT_FAIL);
        }
        List<UserDTO> handledUsers = users.stream()
                .map(user -> BeanUtil.copyProperties(user, UserDTO.class)).collect(Collectors.toList());
        return ResultUtils.success(handledUsers, SELECT_SUCCESS);
    }

    @Override
    public BaseResponse<Boolean> disableUser(Integer userId) {
        User user = getById(userId);
        if (user == null) {
            return ResultUtils.error(false, USER_NOT_EXIST);
        }
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUserId, userId).set(User::getIsValid, 1);
        boolean success = update(wrapper);
        if (success) {
            return ResultUtils.success(true, UPDATE_SUCCESS);
        }
        return ResultUtils.error(false, UPDATE_FAIL);
    }

    @Override
    public BaseResponse<Boolean> deleteUser(Integer userId) {
        User user = getById(userId);
        if (user == null) {
            return ResultUtils.error(false, USER_NOT_EXIST);
        }
        //删除购物车中的信息
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        boolean remove = shoppingCartService.remove(wrapper);
        //删除用户表中的信息
        boolean success = removeById(userId);
        if (remove && success) {
            return ResultUtils.success(true, DELETE_SUCCESS);
        }
        return ResultUtils.error(false, DELETE_FAIL);
    }


}




