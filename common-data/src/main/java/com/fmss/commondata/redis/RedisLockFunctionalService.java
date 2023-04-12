package com.fmss.commondata.redis;

import org.springframework.stereotype.Service;

@Service
public class RedisLockFunctionalService {
    private final RedisUserSessionService redisUserSessionService;

    public void lock(String lockKey, int lockTtl) {
        boolean lockValue = this.redisUserSessionService.acquireLock(lockKey, (long)lockTtl);
        if (!lockValue) {
            throw new RuntimeException("Lock is already exist " + lockKey);
        }
    }

    public void releaseLock(String lockKey) {
        this.redisUserSessionService.releaseLock(lockKey);
    }

    public RedisLockFunctionalService(RedisUserSessionService redisUserSessionService) {
        this.redisUserSessionService = redisUserSessionService;
    }
}