package com.fmss.basketservice.exception;

public class BasketNotFoundException extends RuntimeException{
    public BasketNotFoundException() {
        super("Sepet bulunamadı.");
    }

    public BasketNotFoundException(String message) {
        super(message);
    }
}
