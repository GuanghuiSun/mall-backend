package com.mall.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private Integer userId;
    private String username;
    private Byte isValid;
    private Date createTime;
}
