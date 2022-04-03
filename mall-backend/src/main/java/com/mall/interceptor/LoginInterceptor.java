package com.mall.interceptor;

import com.mall.model.domain.User;
import com.mall.model.domain.UserDTO;
import com.mall.utils.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 登录拦截
 *
 * @authoe sgh
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDTO user = UserHolder.getUser();
        if(user == null) {
//            response.setStatus(401);
            return false;
        }
        return true;
    }
}
