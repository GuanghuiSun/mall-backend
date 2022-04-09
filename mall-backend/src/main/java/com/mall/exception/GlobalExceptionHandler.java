package com.mall.exception;

import com.mall.base.BaseResponse;
import com.mall.base.ErrorCode;
import com.mall.base.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     * @param e 业务异常
     * @param <T> 泛型
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public <T> BaseResponse<T> businessExceptionHandler(BusinessException e) {
        log.error("businessException:" + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     * 处理运行时异常
     * @param e 运行时异常
     * @param <T> 泛型
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public <T> BaseResponse<T> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException:" + e.getMessage(), e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }


}
