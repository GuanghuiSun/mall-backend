package com.mall.exception;

import com.mall.base.ErrorCode;
import com.mall.model.domain.OrderMessageDTO;

import java.io.Serializable;

/**
 * 下单业务异常
 * 用于库存回滚
 *
 * @author sgh
 */
public class OrderException extends BusinessException implements Serializable {

    private static final long serialVersionUID = -6257626920442540529L;
    private OrderMessageDTO orderMessage;

    public OrderException(ErrorCode errorCode, OrderMessageDTO orderMessage) {
        super(errorCode);
        this.orderMessage = orderMessage;
    }

    public OrderException(ErrorCode errorCode, String description, OrderMessageDTO orderMessage) {
        super(errorCode, description);
        this.orderMessage = orderMessage;
    }

    public OrderException(int code, String message, String description, OrderMessageDTO orderMessage) {
        super(code, message, description);
        this.orderMessage = orderMessage;
    }

    public OrderMessageDTO getOrderMessage() {
        return orderMessage;
    }
}
