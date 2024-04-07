package cn.ecnu.eblog.article.config;

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
        ttlConfigs.put(CacheConstant.TAG, new int[]{CacheConstant.TAG_MIN_TTL, CacheConstant.TAG_MAX_TTL});
        ttlConfigs.put(CacheConstant.CATEGORY, new int[]{CacheConstant.CATEGORY_MIN_TTL, CacheConstant.CATEGORY_MAX_TTL});
        ttlConfigs.put(CacheConstant.ARTICLE_DETAIL, new int[]{CacheConstant.ARTICLE_DETAIL_MIN_TTL, CacheConstant.ARTICLE_DETAIL_MAX_TTL});
        ttlConfigs.put(CacheConstant.ARTICLE_TAG, new int[]{CacheConstant.ARTICLE_TAG_MIN_TTL, CacheConstant.ARTICLE_TAG_MAX_TTL});
        ttlConfigs.put(CacheConstant.ARTICLE_PAGE, new int[]{CacheConstant.ARTICLE_PAGE_MIN_TTL, CacheConstant.ARTICLE_PAGE_MAX_TTL});
        return new RandomTtlRedisCacheManager(cacheWriter, redisCacheConfiguration, CacheConstant.COMMON_MIN_TTL, CacheConstant.COMMON_MAX_TTL, ttlConfigs);
    }
}
