package com.mall.model.domain;

import lombok.Data;

/**
 * 订单消息类
 * 用于传递给消息队列
 *
 * @author sgh
 */
@Data
public class OrderMessageDTO {
    private Long orderId;
    private Integer userId;
    private Integer productId;
    private Double productPrice;
    private Integer num;
}
