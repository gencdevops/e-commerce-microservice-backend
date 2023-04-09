package com.fmss.userservice.exeption;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String errorCode;
    private final int httpStatus;
    private final String[] arguments;

    public RestException(String errorCode, int httpStatus) {
        super("RestException:" + errorCode + "," + httpStatus);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.arguments = null;
    }

    public RestException(String errorCode, HttpStatus httpStatus) {
        this(errorCode, httpStatus.value());
    }

    public RestException(String errorCode) {
        this(errorCode, HttpStatus.BAD_REQUEST);
    }

    public RestException(String errorCode, Throwable cause, String... arguments) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.httpStatus = 404;
        this.arguments = arguments;
    }

    public RestException(String errorCode, String... arguments) {
        this(errorCode, null, arguments);
    }

    public static void throwIfNull(Object nullable, String code) {
        if (nullable == null) {
            throw new RestException(code);
        }
    }

    public static void throwIfTrue(boolean condition, String code) {
        if (condition) {
            throw new RestException(code);
        }
    }

    public static void throwIfFalse(boolean condition, String code) {
        if (!condition) {
            throw new RestException(code);
        }
    }
}
