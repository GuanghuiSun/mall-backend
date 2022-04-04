package com.mall.base;

public class ResultUtils {
    /**
     * 响应成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data,String message){
        return new BaseResponse<>("001",data,message);
    }

    public static <T> BaseResponse<T> error(T data,String message){
        return new BaseResponse<>("-1",data,message);
    }

    public static <T> BaseResponse<T> success(String code, T data,String message){
        return new BaseResponse<>(code,data,message);
    }

    public static <T> BaseResponse<T> error(String code, T data,String message){
        return new BaseResponse<>(code,data,message);
    }

    public static <T> BaseResponse<T> fail(String message){
        return new BaseResponse<>("-1",null,message);
    }
}
