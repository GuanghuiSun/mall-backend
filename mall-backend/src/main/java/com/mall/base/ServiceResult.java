package com.mall.base;

import lombok.Data;

/**
 * 业务层处理数据结果
 */
@Data
public class ServiceResult {
    private int code;
    private String message;
    public ServiceResult(int code, String message){
        this.code = code;
        this.message = message;
    }
}
