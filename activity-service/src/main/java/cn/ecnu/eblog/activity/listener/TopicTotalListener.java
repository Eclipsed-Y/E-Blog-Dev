package cn.ecnu.eblog.activity.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "topic.woman")
public class TopicTotalListener {
    @RabbitHandler
    public void process(String testMessage) throws InterruptedException {
        Thread.sleep(500);
        System.out.println("TopicTotalReceiver消费者收到消息  : " + testMessage);
    }
}
