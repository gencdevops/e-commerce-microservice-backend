package com.fmss.apigateway.exception;


public class CrackServiceNotAvailableException extends RuntimeException{
    public CrackServiceNotAvailableException(String message) {
        super(message);
    }

    public CrackServiceNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
