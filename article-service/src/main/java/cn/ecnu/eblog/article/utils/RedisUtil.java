package cn.ecnu.eblog.article.utils;

import cn.ecnu.eblog.common.constant.CacheConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
