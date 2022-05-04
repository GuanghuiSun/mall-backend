package com.mall.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * 自定义实现不可路由消息处理
 *
 * @author sgh
 */
@Component
@Slf4j
public class RabbitMQBack implements RabbitTemplate.ReturnsCallback, RabbitTemplate.ConfirmCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ConnectionFactory connectionFactory;

    /**
     * 将实现类注入
     * 注入顺序：Construct->@Autowired->@PostConstruct
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setReturnsCallback(this);
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setConnectionFactory(connectionFactory);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        if (correlationData != null) {
            correlationData.getId();
            String id = correlationData.getId();
            if (ack) {
                log.info("{}交换机接收到ID为{}的消息并进行处理", Objects.requireNonNull(correlationData.getReturned()).getExchange(), id);
            } else {
                log.warn("{}交换机接收ID为{}的消息失败，原因是:{}", Objects.requireNonNull(correlationData.getReturned()).getExchange(), id, cause);
            }
        } else {
            log.warn("消息传输失败！");
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.warn("不可路由消息相应码 : " + returned.getReplyCode());
        log.warn("不可路由消息主体 message : " + returned.getMessage());
        log.warn("不可路由消息描述：" + returned.getReplyText());
        log.warn("不可路由消息使用的交换器 exchange : " + returned.getMessage());
        log.warn("不可路由消息使用的路由键 routingKey : " + returned.getRoutingKey());
    }
}
