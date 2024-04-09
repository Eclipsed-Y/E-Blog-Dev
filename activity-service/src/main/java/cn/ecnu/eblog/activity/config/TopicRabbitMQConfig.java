package cn.ecnu.eblog.activity.config;

import cn.ecnu.eblog.common.constant.MQConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitMQConfig {
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
     * 点赞队列
     * @return
     */
    @Bean
    public Queue likeQueue(){
        return QueueBuilder.durable(MQConstant.LIKE_QUEUE).deadLetterExchange(MQConstant.DDL_EXCHANGE).ttl(1000).build();
    }

    /**
     * 关注队列
     * @return
     */
    @Bean
    public Queue attentionQueue(){
        return QueueBuilder.durable(MQConstant.ATTENTION_QUEUE).deadLetterExchange(MQConstant.DDL_EXCHANGE).ttl(1000).build();
    }

    /**
     * 评论死信队列
     * @return
     */
    @Bean
    public Queue commentDdlQueue() {
        return new Queue(MQConstant.COMMENT_DDL_QUEUE, true, false, false);
    }
    /**
     * 点赞死信队列
     * @return
     */
    @Bean
    public Queue likeDdlQueue() {
        return new Queue(MQConstant.LIKE_DDL_QUEUE, true, false, false);
    }

    /**
     * 关注死信队列
     * @return
     */
    @Bean
    public Queue attentionDdlQueue() {
        return new Queue(MQConstant.ATTENTION_DDL_QUEUE, true, false, false);
    }

    /**
     * 评论binding
     * @return
     */
    @Bean
    public Binding commentBindingTopicExchange() {
        return BindingBuilder.bind(commentQueue()).to(topicExchange()).with(MQConstant.COMMENT);
    }

    /**
     * 点赞binding
     * @return
     */
    @Bean
    public Binding likeBindingTopicExchange(){
        return BindingBuilder.bind(likeQueue()).to(topicExchange()).with(MQConstant.LIKE);
    }

    /**
     * 关注binding
     * @return
     */
    @Bean
    public Binding attentionBindingTopicExchange(){
        return BindingBuilder.bind(attentionQueue()).to(topicExchange()).with(MQConstant.ATTENTION);
    }

    /**
     * 评论死信binding
     * @return
     */
    @Bean
    public Binding commentBindingDdlExchange() {
        return BindingBuilder.bind(commentDdlQueue()).to(ddlExchange()).with(MQConstant.COMMENT);
    }

    /**
     * 点赞死信binding
     * @return
     */
    @Bean
    public Binding likeBindingDdlExchange() {
        return BindingBuilder.bind(likeDdlQueue()).to(ddlExchange()).with(MQConstant.LIKE);
    }
    /**
     * 关注死信binding
     * @return
     */
    @Bean
    public Binding attentionBindingDdlExchange() {
        return BindingBuilder.bind(attentionDdlQueue()).to(ddlExchange()).with(MQConstant.ATTENTION);
    }

}