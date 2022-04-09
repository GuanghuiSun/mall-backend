package com.mall.exception;


import com.mall.base.ErrorCode;

/**
 * 自定义异常类
 *
 * @author sgh
 */
public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = 3265798131753056844L;
    private final int code;
    private final String description;

    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public BusinessException(int code, String message, String description){
        super(message);
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }
}
