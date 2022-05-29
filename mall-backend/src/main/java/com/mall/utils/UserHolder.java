package com.mall.utils;

import com.mall.model.domain.UserDTO;

/**
 * 使用ThreadLocal存储用户信息
 *
 * @author sgh
 */
public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
