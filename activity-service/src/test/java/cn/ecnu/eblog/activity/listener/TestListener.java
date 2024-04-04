package cn.ecnu.eblog.activity.listener;

import cn.ecnu.eblog.activity.event.TestEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Async
public class TestListener {
    @EventListener
    public void testListener(TestEvent testEvent) throws InterruptedException {
        Thread.sleep(10000);
        System.out.println("接受到消息：" + testEvent.getMsg());
    }
}
