package cn.ecnu.eblog.activity.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    public void remove(String key, String prefix){
        Set<String> keys = redisTemplate.keys(key + "::" + prefix + "*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }
}
