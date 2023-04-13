package com.fmss.basketservice.exception;

public class BasketItemNotFound extends RuntimeException{
    public BasketItemNotFound() {
    }

    public BasketItemNotFound(String message) {
        super(message);
    }
}
