package com.mall.base;

import lombok.Data;

/**
 * @author sgh
 * @description 响应消息类
 */
@Data
public class BaseResponse<T> {
    private String code;
    private Object data;
    private String msg;

    public BaseResponse(String code, Object data, String message){
        this.code=code;
        this.data=data;
        this.msg=message;
    }
}
