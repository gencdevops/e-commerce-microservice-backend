package com.fmss.productservice.exception;

public class PreconditionException extends RuntimeException{
    public PreconditionException(String message) {
        super(message);
    }
}
