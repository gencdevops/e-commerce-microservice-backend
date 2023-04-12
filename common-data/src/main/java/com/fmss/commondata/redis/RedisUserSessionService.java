package com.fmss.commondata.redis;


import com.fmss.commondata.exception.RedisSessionDataNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisUserSessionService {
    private static final Logger log = LoggerFactory.getLogger(RedisUserSessionService.class);
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${redis.service.prefix}")
    private String redisDomainPrefix;
    @Value("${redis.service.ttl}")
    private long redisSessionTimeToLive;

    @Autowired
    public RedisUserSessionService(@Qualifier("fmssRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Map<Object, Object> getWholeSessionDataByKey(String key) {
        return this.redisTemplate.opsForHash().entries(this.redisDomainPrefix.concat(key));
    }

    public <T> T getPartialSessionData(String key, String hashKey, Class<T> targetClass) {
        return targetClass.cast(this.redisTemplate.opsForHash().get(this.redisDomainPrefix.concat(key), hashKey));
    }

    public List<Object> getPartialSessionDataList(String key, Collection<Object> hashKeyList) {
        return this.redisTemplate.opsForHash().multiGet(this.redisDomainPrefix.concat(key), hashKeyList);
    }

    @Async
    public <T> void addDataToSession(String key, String hashKey, T object) {
        this.redisTemplate.opsForHash().put(this.redisDomainPrefix.concat(key), hashKey, object);
        this.redisTemplate.expire(this.redisDomainPrefix.concat(key), this.redisSessionTimeToLive, TimeUnit.SECONDS);
    }

    public <T> void addDataToSessionSync(String key, String hashKey, T object) {
        this.redisTemplate.opsForHash().put(this.redisDomainPrefix.concat(key), hashKey, object);
        this.redisTemplate.expire(this.redisDomainPrefix.concat(key), this.redisSessionTimeToLive, TimeUnit.SECONDS);
    }

    public <T> void updatePartialSessionData(String key, String hashKey, T object) {
        if (Boolean.FALSE.equals(this.redisTemplate.opsForHash().hasKey(this.redisDomainPrefix.concat(key), hashKey))) {
            throw new RedisSessionDataNotFound();
        } else {
            this.redisTemplate.opsForHash().put(this.redisDomainPrefix.concat(key), hashKey, object);
        }
    }

    public void removePartialSessionData(String key, String hashKey) {
        this.redisTemplate.opsForHash().delete(this.redisDomainPrefix.concat(key), new Object[]{hashKey});
    }

    public void removePartialSessionData(String key, String... hashKeyList) {
        Arrays.stream(hashKeyList).forEach((hashKey) -> {
            this.redisTemplate.opsForHash().delete(this.redisDomainPrefix.concat(key), new Object[]{hashKey});
        });
    }

    public void removeSessionDataByKey(String key) {
        this.redisTemplate.opsForHash().getOperations().delete(this.redisDomainPrefix.concat(key));
    }

    public <T> Boolean acquireLock(String lockKey, long timeoutInMilliseconds) {
        return this.redisTemplate.opsForValue().setIfAbsent(this.getLockKey(lockKey), "Lock for " + lockKey, timeoutInMilliseconds, TimeUnit.MILLISECONDS);
    }

    public <T> void releaseLock(String lockKey) {
        this.redisTemplate.opsForValue().getOperations().delete(this.getLockKey(lockKey));
    }

    private String getLockKey(String lockKey) {
        return this.redisDomainPrefix.concat("LockKey." + lockKey);
    }
}
