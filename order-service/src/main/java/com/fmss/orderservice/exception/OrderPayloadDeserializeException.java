package com.fmss.orderservice.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderPayloadDeserializeException extends RuntimeException{
    public OrderPayloadDeserializeException(String message){
        super(message);
    }
}
