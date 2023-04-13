package com.fmss.orderservice.exception;



import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String message){
        super(message);
    }
}
