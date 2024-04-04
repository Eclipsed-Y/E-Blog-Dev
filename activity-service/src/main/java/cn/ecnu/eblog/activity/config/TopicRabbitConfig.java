package cn.ecnu.eblog.activity.config;

import cn.ecnu.eblog.common.constant.MQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitConfig {
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(MQConstant.EXCHANGE);
    }

    @Bean
    public Queue commentQueue() {
        return new Queue(MQConstant.COMMENT);
    }

    @Bean
    public Binding bindingExchangeMessage() {
        return BindingBuilder.bind(commentQueue()).to(exchange()).with(MQConstant.COMMENT);
    }

}