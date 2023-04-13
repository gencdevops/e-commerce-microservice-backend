package com.fmss.productservice.configuration;

import com.fmss.commondata.exception.RedisSessionDataNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${redis.service.prefix}")
    private String redisDomainPrefix;
    @Value("${redis.service.ttl}")
    private long redisSessionTimeToLive;


    @Autowired
    public RedisCacheService(@Qualifier("fmssRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Async
    public <T> void addDataTOCacheWithHashKey(String key, String hashKey, T object) {
        this.redisTemplate.opsForHash().put(this.redisDomainPrefix.concat(key), hashKey, object);
        this.redisTemplate.expire(this.redisDomainPrefix.concat(key), this.redisSessionTimeToLive, TimeUnit.SECONDS);
    }

    @Async
    public <T> void addDataTOCache(String key, T object) {
        redisTemplate.opsForList().leftPush(key, object);
        this.redisTemplate.expire(this.redisDomainPrefix.concat(key), this.redisSessionTimeToLive, TimeUnit.SECONDS);
    }

    public <T> T getPartialSessionData(String key, String hashKey, Class<T> targetClass) {
        return targetClass.cast(redisTemplate.opsForHash().get(redisDomainPrefix.concat(key), hashKey));
    }

    public void removeUserTokenFromRedis(String key) {
        this.redisTemplate.opsForHash().getOperations().delete(this.redisDomainPrefix.concat(key));

    }

    @Async
    public <T> void writeListToCacheWithHashKey(String key, String hashKey, T object) {
        this.redisTemplate.opsForHash().putIfAbsent(this.redisDomainPrefix.concat(key), hashKey, object);
        this.redisTemplate.expire(this.redisDomainPrefix.concat(key), this.redisSessionTimeToLive, TimeUnit.SECONDS);
    }

    @Async
    public <T> void writeListToCachePutAll(String key, Map<String, Object> mapList) {
        this.redisTemplate.opsForHash().putAll(this.redisDomainPrefix.concat(key), mapList);
        this.redisTemplate.expire(this.redisDomainPrefix.concat(key), this.redisSessionTimeToLive, TimeUnit.SECONDS);
    }

    public <T> void updatePartialCacheData(String key, String hashKey, T object) {
        if (Boolean.FALSE.equals(this.redisTemplate.opsForHash().hasKey(this.redisDomainPrefix.concat(key), hashKey))) {
            throw new RedisSessionDataNotFound();
        } else {
            this.redisTemplate.opsForHash().put(this.redisDomainPrefix.concat(key), hashKey, object);
        }
    }


}
