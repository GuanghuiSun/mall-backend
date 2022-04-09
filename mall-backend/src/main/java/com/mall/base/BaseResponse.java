package com.mall.base;

import lombok.Data;

/**
 * @author sgh
 * @description 响应消息类
 */
@Data
public class BaseResponse<T> {
    private int code;
    private T data;
    private String msg;
    private String description;

    public BaseResponse(int code, T data, String message,String description){
        this.code=code;
        this.data=data;
        this.msg=message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message){
        this(code,data,message,"");
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(), null, errorCode.getMessage(),errorCode.getDescription());
    }
}
