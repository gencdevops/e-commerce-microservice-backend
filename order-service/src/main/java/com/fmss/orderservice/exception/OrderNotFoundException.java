package com.fmss.orderservice.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException() {
        super("Order bulunamadı.");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
