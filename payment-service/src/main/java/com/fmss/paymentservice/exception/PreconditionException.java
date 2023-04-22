package com.fmss.paymentservice.exception;

public class PreconditionException extends RuntimeException{
    public PreconditionException(String message) {
        super(message);
    }
}
