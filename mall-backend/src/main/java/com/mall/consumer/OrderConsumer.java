package com.mall.consumer;

import cn.hutool.json.JSONUtil;
import com.mall.config.RabbitMQConfig;
import com.mall.model.domain.OrderMessageDTO;
import com.mall.service.OrdersService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 订单消息监听处理类
 */
@Component
@Slf4j
public class OrderConsumer {

    @Resource
    private OrdersService ordersService;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void orderListener(Message message, Channel channel) throws IOException {
        OrderMessageDTO orderMessageDTO = JSONUtil.toBean(new String(message.getBody()), OrderMessageDTO.class);
        Boolean success = ordersService.handleOrder(orderMessageDTO);
        if (Boolean.TRUE.equals(success)) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);//手动应答
        }
    }


}
