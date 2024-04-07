package cn.ecnu.eblog.activity.config;

import cn.ecnu.eblog.common.constant.CacheConstant;
import cn.ecnu.eblog.common.redis.RandomTtlRedisCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
        HashMap<String, int[]> ttlConfigs = new HashMap<>();
        ttlConfigs.put(CacheConstant.COMMENT_PAGE, new int[]{CacheConstant.COMMENT_PAGE_MIN_TTL, CacheConstant.COMMENT_PAGE_MAX_TTL});
        ttlConfigs.put(CacheConstant.SECOND_COMMENT_PAGE, new int[]{CacheConstant.SECOND_COMMENT_PAGE_MIN_TTL, CacheConstant.SECOND_COMMENT_PAGE_MAX_TTL});
        return new RandomTtlRedisCacheManager(cacheWriter, redisCacheConfiguration, CacheConstant.COMMON_MIN_TTL, CacheConstant.COMMON_MAX_TTL, ttlConfigs);
    }
}
