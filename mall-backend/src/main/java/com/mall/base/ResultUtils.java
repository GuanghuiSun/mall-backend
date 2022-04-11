package com.mall.base;

public class ResultUtils {
    /**
     * 响应成功
     *
     * @param data 响应数据
     * @param <T> 数据类型
     * @return
     */
    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>(200, data, message,message);
    }

    public static <T> BaseResponse<T> success(int code, T data, String message) {
        return new BaseResponse<>(code, data, message,message);
    }

    public static <T> BaseResponse<T> error(int code, T data, String message) {
        return new BaseResponse<>(code, data, message,message);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static <T> BaseResponse<T> error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }
}
