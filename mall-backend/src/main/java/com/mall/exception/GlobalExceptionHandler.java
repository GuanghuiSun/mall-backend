package com.mall.exception;

import com.mall.base.BaseResponse;
import com.mall.base.ErrorCode;
import com.mall.base.ResultUtils;
import com.mall.model.domain.OrderMessageDTO;
import com.mall.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;

import static com.mall.constant.RedisConstant.INVENTORY_KEY;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private OrdersService ordersService;

    /**
     * 下单业务异常处理器 用于回滚库存
     *
     * @param e   下单业务异常
     * @param <T> 类型
     * @return 通用响应消息返回体
     */
    @ExceptionHandler(OrderException.class)
    public <T> BaseResponse<T> orderExceptionHandler(OrderException e) {
        log.error("orderException:" + e.getMessage(), e);
        OrderMessageDTO orderMessage = e.getOrderMessage();
        if (Boolean.FALSE.equals(orderMessage.getIdConsumed())) {
            log.error("firstTry:errorOrderMessage:" + e.getOrderMessage());
            orderMessage.setIdConsumed(Boolean.TRUE);
            ordersService.sendMessage(orderMessage);
            return null;
        }
        log.error("secondTry:errorOrderMessage:" + e.getOrderMessage());
        String key = INVENTORY_KEY + ":" + orderMessage.getProductId();
        stringRedisTemplate.opsForValue().increment(key, orderMessage.getNum());
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     * 处理业务异常
     *
     * @param e   业务异常
     * @param <T> 泛型
     * @return 通用响应消息返回体
     */
    @ExceptionHandler(BusinessException.class)
    public <T> BaseResponse<T> businessExceptionHandler(BusinessException e) {
        log.error("businessException:" + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     * 处理运行时异常
     *
     * @param e   运行时异常
     * @param <T> 泛型
     * @return 通用响应消息返回体
     */
    @ExceptionHandler(RuntimeException.class)
    public <T> BaseResponse<T> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException:" + e.getMessage(), e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }


}
