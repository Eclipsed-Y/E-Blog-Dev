package cn.ecnu.eblog.common.redis;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.Random;

public class RandomTtlRedisCache extends RedisCache {
    /**
     * 最小ttl，单位毫秒
     */
    private int minTtl;
    /**
     * 最大ttl，单位毫秒
     */
    private int maxTtl;
    private Random random = new Random();
    private final String name;
    private final RedisCacheWriter cacheWriter;
    protected RandomTtlRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig, int minTtl, int maxTtl) {
        super(name, cacheWriter, cacheConfig);
        this.minTtl = minTtl;
        this.maxTtl = maxTtl;
        this.name = name;
        this.cacheWriter = cacheWriter;

    }

    @Override
    public void put(Object key, @Nullable Object value) {
        Object cacheValue = this.preProcessCacheValue(value);
        if (!this.isAllowNullValues() && cacheValue == null) {
            throw new IllegalArgumentException(String.format("Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == null\")' or configure RedisCache to allow 'null' via RedisCacheConfiguration.", this.name));
        } else {
            this.cacheWriter.put(this.name, this.createAndConvertCacheKey(key), this.serializeCacheValue(cacheValue), this.getRandomTtl());
        }
    }

    @Override
    public Cache.ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
        Object cacheValue = this.preProcessCacheValue(value);
        if (!this.isAllowNullValues() && cacheValue == null) {
            return this.get(key);
        } else {
            byte[] result = this.cacheWriter.putIfAbsent(this.name, this.createAndConvertCacheKey(key), this.serializeCacheValue(cacheValue), this.getRandomTtl());
            return result == null ? null : new SimpleValueWrapper(this.fromStoreValue(this.deserializeCacheValue(result)));
        }
    }

    private Duration getRandomTtl() {
        int randomTtl = minTtl + random.nextInt(maxTtl - minTtl + 1);
        return Duration.ofMillis(randomTtl);

    }

    private byte[] createAndConvertCacheKey(Object key) {
        return this.serializeCacheKey(this.createCacheKey(key));
    }
}
