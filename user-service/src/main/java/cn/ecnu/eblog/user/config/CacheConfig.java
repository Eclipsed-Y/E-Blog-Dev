package cn.ecnu.eblog.user.config;

import cn.ecnu.eblog.common.redis.RandomTtlRedisCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;

@Configuration
public class CacheConfig {
    @Bean
    public RedisTemplate<String, Object> redisClient(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RandomTtlRedisCacheManager redisCacheManager(RedisConnectionFactory factory){
        RedisCacheWriter cacheWriter = RedisCacheWriter.lockingRedisCacheWriter(factory);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        // userInfo 缓存设置15 - 20分钟
        // article 缓存设置30 - 40分钟
        HashMap<String, int[]> ttlConfigs = new HashMap<>();
        ttlConfigs.put("userInfo", new int[]{900000, 1200000});
        ttlConfigs.put("article", new int[]{1800000, 2400000});
        return new RandomTtlRedisCacheManager(cacheWriter, redisCacheConfiguration, 30000, 120000, ttlConfigs);
    }
}
