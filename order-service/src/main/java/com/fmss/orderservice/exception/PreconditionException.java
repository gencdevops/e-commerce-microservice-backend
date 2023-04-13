package com.fmss.orderservice.exception;

public class PreconditionException extends RuntimeException{
    public PreconditionException(String message) {
        super(message);
    }
}
