package com.mall.base;


import static com.mall.constant.MessageConstant.*;

/**
 * 错误码
 *
 * @author sgh
 */
public enum ErrorCode {

    PARAMS_NULL_ERROR(40000,REQUEST_PARAM_EMPTY_ERROR,REQUEST_PARAM_EMPTY_ERROR),
    PARAMS_PATTERN_ERROR(40001,REQUEST_PARAM_ERROR,REQUEST_PARAM_ERROR),
    GET_MESSAGE_ERROR(50001,GET_ERROR,GET_ERROR),
    NOT_LOGIN_ERROR(40100,LOGIN_ERROR,LOGIN_ERROR),
    REQUEST_SERVICE_ERROR(40002,REQUEST_ERROR,REQUEST_ERROR),
    SYSTEM_ERROR(50000,SERVER_INTERNAL_ERROR,SERVER_INTERNAL_ERROR);


    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code, String message, String description){
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
