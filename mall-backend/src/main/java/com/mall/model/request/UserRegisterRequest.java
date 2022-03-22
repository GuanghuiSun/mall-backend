package com.mall.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -8824542536033330104L;
    private String username;
    private String password;
    private String checkPassword;
}
