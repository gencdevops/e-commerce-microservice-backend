package com.fmss.basketservice.exception;

public class BasketNotFoundException extends RuntimeException{
    public BasketNotFoundException() {
        super("Seper bulunamadı.");
    }

    public BasketNotFoundException(String message) {
        super(message);
    }
}
