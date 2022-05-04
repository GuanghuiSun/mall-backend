package com.mall.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sgh
 *
 * 消息队列RabbitMQ配置类
 */
@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private Integer port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.publisher-returns}")
    private Boolean returns;
    @Value(("${spring.rabbitmq.virtual-host}"))
    private String virtualHost;

    public static final String ORDER_EXCHANGE = "orderExchange";
    public static final String ORDER_QUEUE = "orderQueue";
    public static final String ORDER_ROUTING_KEY = "orderKey";


    /**
     * 创建RabbitMQ连接池
     * @return 连接池
     */
    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(host);
        cachingConnectionFactory.setPort(port);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        cachingConnectionFactory.setPublisherReturns(returns);
        cachingConnectionFactory.setVirtualHost(virtualHost);//开启连接池的ReturnCallBack支持
        cachingConnectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);//开启连接池的publish-confirm-type支持
        return cachingConnectionFactory;
    }

    /**
     *
     * @param connectionFactory 连接池
     * @return 消息监听容器
     */
    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);//关闭自动ack
        factory.setConnectionFactory(connectionFactory);//使用连接池
        factory.setPrefetchCount(1);//设置QOS
        factory.setMessageConverter(new Jackson2JsonMessageConverter());//设置消息转换器为JSON
        return factory;
    }

    @Bean
    public DirectExchange orderExchange(){
        return ExchangeBuilder.directExchange(ORDER_EXCHANGE)
                .durable(true)//持久化
                .build();
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE).build();
    }

    @Bean
    public Binding orderBinding(@Qualifier("orderExchange")DirectExchange orderExchange,
                                @Qualifier("orderQueue")Queue orderQueue) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_ROUTING_KEY);
    }
}
