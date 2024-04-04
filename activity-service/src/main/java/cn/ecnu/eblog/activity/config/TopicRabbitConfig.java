package cn.ecnu.eblog.activity.config;

import cn.ecnu.eblog.common.constant.MQConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitConfig {
    /**
     * 普通交换机
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(MQConstant.TOPIC_EXCHANGE);
    }

    /**
     * 死信交换机
     * @return
     */
    @Bean
    public TopicExchange ddlExchange() {
        return new TopicExchange(MQConstant.DDL_EXCHANGE, true, false);
    }

    /**
     * 评论队列
     * @return
     */
    @Bean
    public Queue commentQueue() {
        return QueueBuilder.durable(MQConstant.COMMENT_QUEUE).deadLetterExchange(MQConstant.DDL_EXCHANGE).ttl(2000).build();  // 超时2s就放入死信队列
    }
    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue ddlQueue() {
        return new Queue(MQConstant.DDL_QUEUE, true, false, false);
    }

    /**
     * 评论binding
     * @return
     */
    @Bean
    public Binding bindingTopicExchange() {
        return BindingBuilder.bind(commentQueue()).to(topicExchange()).with(MQConstant.COMMENT);
    }

    /**
     * 死信bingding
     * @return
     */
    @Bean
    public Binding bindingDdlExchange() {
        return BindingBuilder.bind(ddlQueue()).to(ddlExchange()).with(MQConstant.COMMENT);
    }

}