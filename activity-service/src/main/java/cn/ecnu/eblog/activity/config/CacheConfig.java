package cn.ecnu.eblog.activity.config;

import cn.ecnu.eblog.common.constant.CacheConstant;
import cn.ecnu.eblog.common.redis.RandomTtlRedisCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;

@Configuration
public class CacheConfig {
    @Bean
    public RedisTemplate<String, Object> redisClient(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Object.class));
        return redisTemplate;
    }

    @Bean()
    @Primary
    public RandomTtlRedisCacheManager redisCacheManager(RedisConnectionFactory factory){
        RedisCacheWriter cacheWriter = RedisCacheWriter.lockingRedisCacheWriter(factory);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        HashMap<String, int[]> ttlConfigs = new HashMap<>();
        ttlConfigs.put(CacheConstant.COMMENT_PAGE, new int[]{CacheConstant.COMMENT_PAGE_MIN_TTL, CacheConstant.COMMENT_PAGE_MAX_TTL});
        ttlConfigs.put(CacheConstant.SECOND_COMMENT_PAGE, new int[]{CacheConstant.SECOND_COMMENT_PAGE_MIN_TTL, CacheConstant.SECOND_COMMENT_PAGE_MAX_TTL});
        return new RandomTtlRedisCacheManager(cacheWriter, redisCacheConfiguration, CacheConstant.COMMON_MIN_TTL, CacheConstant.COMMON_MAX_TTL, ttlConfigs);
    }

    @Bean
    public RandomTtlRedisCacheManager redisCacheManagerForNums(RedisConnectionFactory factory){
        RedisCacheWriter cacheWriter = RedisCacheWriter.lockingRedisCacheWriter(factory);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer(Object.class)));
        HashMap<String, int[]> ttlConfigs = new HashMap<>();
        ttlConfigs.put(CacheConstant.LIKED, new int[]{CacheConstant.LIKED_MIN_TTL, CacheConstant.LIKED_MAX_TTL});
        ttlConfigs.put(CacheConstant.LIKE_COUNT, new int[]{CacheConstant.LIKE_COUNT_MIN_TTL, CacheConstant.LIKE_COUNT_MAX_TTL});
        return new RandomTtlRedisCacheManager(cacheWriter, redisCacheConfiguration, CacheConstant.COMMON_MIN_TTL, CacheConstant.COMMON_MAX_TTL, ttlConfigs);
    }
}
