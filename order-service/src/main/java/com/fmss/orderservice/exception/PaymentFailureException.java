package com.fmss.orderservice.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentFailureException extends RuntimeException{
    public PaymentFailureException(String message){
        super(message);
    }
}
