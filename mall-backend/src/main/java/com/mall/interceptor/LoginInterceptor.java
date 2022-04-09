package com.mall.interceptor;

import com.mall.base.ErrorCode;
import com.mall.exception.BusinessException;
import com.mall.model.domain.UserDTO;
import com.mall.utils.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 登录拦截
 *
 * @author sgh
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserDTO user = UserHolder.getUser();
        if(user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return true;
    }
}
