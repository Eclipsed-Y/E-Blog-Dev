package cn.ecnu.eblog.activity.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = {"topic.man"})
public class TopicManListener {
    @RabbitHandler
    public void process(String testMessage) throws InterruptedException {
        Thread.sleep(500);
        System.out.println("TopicManReceiver消费者收到消息  : " + testMessage);
    }
}
