package cn.ecnu.eblog.activity;

import cn.ecnu.eblog.activity.event.TestEvent;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class ActivityTest {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
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
    @Test
    public void testLua(){
        List<String> keys = new ArrayList<>();
        keys.add("liked::108_22");
        keys.add("likeCount::22");
        // 执行 lua 脚本
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("likeCheck.lua")));
        // 指定返回类型
        redisScript.setResultType(Long.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Long result = redisTemplate.execute(redisScript, keys, 600, 600);
        System.out.println(result);
    }
}
