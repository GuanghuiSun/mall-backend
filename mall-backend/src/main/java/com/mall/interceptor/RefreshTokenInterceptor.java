package com.mall.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.mall.model.domain.UserDTO;
import com.mall.utils.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.mall.constant.RedisConstant.LOGIN_USER_TTL;
import static com.mall.constant.RedisConstant.USER_LOGIN_STATE;

/**
 * 刷新登录token有效期
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头中的token
        StringBuffer requestURL = request.getRequestURL();

        System.out.println("requestURL = " + requestURL);
        String token = request.getHeader("authorization");
        if(StringUtils.isAnyBlank(token)) {
            return true;
        }
        //根据token获取用户
        String tokenKey = USER_LOGIN_STATE +":"+ token;
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(tokenKey);
        if(entries.isEmpty()){
            return true;
        }
        //转换为UserDTO对象
        UserDTO userDTO = BeanUtil.fillBeanWithMap(entries, new UserDTO(), false);
        //保存信息到ThreadLocal中
        UserHolder.saveUser(userDTO);
        System.out.println(UserHolder.getUser() + "已登录！");
//        刷新有效期
        stringRedisTemplate.expire(tokenKey,LOGIN_USER_TTL, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println(UserHolder.getUser() + "已退出！");
        UserHolder.removeUser();
    }
}
