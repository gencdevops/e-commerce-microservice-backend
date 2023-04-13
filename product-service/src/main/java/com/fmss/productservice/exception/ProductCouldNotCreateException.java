package com.fmss.productservice.exception;

public class ProductCouldNotCreateException extends RuntimeException{
    public ProductCouldNotCreateException(String message) {
        super(message);
    }
}
