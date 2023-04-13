package com.fmss.commondata.exception;

public class RedisSessionDataNotFound extends RuntimeException {
    public RedisSessionDataNotFound() {
    }

    public RedisSessionDataNotFound(String message) {
        super(message);
    }

    public RedisSessionDataNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisSessionDataNotFound(Throwable cause) {
        super(cause);
    }

    protected RedisSessionDataNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

