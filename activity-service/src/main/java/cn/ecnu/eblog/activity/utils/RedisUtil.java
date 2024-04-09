package cn.ecnu.eblog.activity.utils;

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
    public void likeCheck(Long articleId, Long userId){
        List<String> keys = new ArrayList<>();
        keys.add(CacheConstant.LIKED + "::" + articleId + "_" + userId);
        keys.add(CacheConstant.LIKE_COUNT + "::" + articleId);
        // 执行 lua 脚本
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("likeCheck.lua")));
        // 指定返回类型
        redisScript.setResultType(Long.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Random random = new Random();
        int arg1 = (CacheConstant.LIKED_MIN_TTL + random.nextInt(CacheConstant.LIKED_MAX_TTL - CacheConstant.LIKED_MIN_TTL + 1000)) / 1000;
        int arg2 = (CacheConstant.LIKE_COUNT_MIN_TTL + random.nextInt(CacheConstant.LIKE_COUNT_MAX_TTL - CacheConstant.LIKE_COUNT_MIN_TTL + 1000)) / 1000;
        Long result = redisTemplate.execute(redisScript, keys, arg1, arg2);
    }

    public void attentionCheck(Long userId, Long followerId) {
        List<String> keys = new ArrayList<>();
        keys.add(CacheConstant.HAS_ATTENTION + "::" + userId + "_" + followerId);
        keys.add(CacheConstant.ATTENTION_COUNT + "::" + userId);
        // 执行 lua 脚本
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("attentionCheck.lua")));
        // 指定返回类型
        redisScript.setResultType(Long.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Random random = new Random();
        int arg1 = (CacheConstant.HAS_ATTENTION_MIN_TTL + random.nextInt(CacheConstant.HAS_ATTENTION_MAX_TTL - CacheConstant.HAS_ATTENTION_MIN_TTL + 1000)) / 1000;
        int arg2 = (CacheConstant.ATTENTION_COUNT_MIN_TTL + random.nextInt(CacheConstant.ATTENTION_COUNT_MAX_TTL - CacheConstant.ATTENTION_COUNT_MIN_TTL + 1000)) / 1000;
        Long result = redisTemplate.execute(redisScript, keys, arg1, arg2);
    }
}
