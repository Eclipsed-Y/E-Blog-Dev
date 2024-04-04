package cn.ecnu.eblog.activity;

import cn.ecnu.eblog.activity.event.TestEvent;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest
public class ActivityTest {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Test
    public void testEvent() throws InterruptedException {
        applicationEventPublisher.publishEvent(new TestEvent(this, "hello"));
        System.out.println("发送完毕");
        Thread.sleep(20000);
    }
    @Test
    public void testRabbitMQ() throws InterruptedException {
        rabbitTemplate.convertAndSend("topicExchange", "topic.man", "man~~~");
        rabbitTemplate.convertAndSend("topicExchange", "topic.woman", "woman~~~");
        System.out.println("123456");
        Thread.sleep(10000);
    }
}
