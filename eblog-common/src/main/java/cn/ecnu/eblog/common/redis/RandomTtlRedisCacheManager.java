package cn.ecnu.eblog.common.redis;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

public class RandomTtlRedisCacheManager extends RedisCacheManager{
    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfig;
    private final int minTtl;
    private final int maxTtl;
    private final Map<String, int[]> ttlConfigs;

    public RandomTtlRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, int minTtl, int maxTtl, Map<String, int[]> ttlConfigs) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
        this.minTtl = minTtl;
        this.maxTtl = maxTtl;
        this.ttlConfigs = ttlConfigs;
    }

    @Override
    protected RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
        return new RandomTtlRedisCache(name, this.cacheWriter, cacheConfig != null ? cacheConfig : this.defaultCacheConfig, minTtl, maxTtl);
    }

    @Override
    protected Collection<RedisCache> loadCaches() {
        List<RedisCache> caches = new LinkedList<>();
        ttlConfigs.forEach((name, ttlArr) -> caches.add(new RandomTtlRedisCache(name, cacheWriter, defaultCacheConfig, ttlArr[0], ttlArr[1])));
        return caches;
    }
}
