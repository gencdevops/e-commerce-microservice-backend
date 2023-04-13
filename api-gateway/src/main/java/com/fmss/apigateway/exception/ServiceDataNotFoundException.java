package com.fmss.apigateway.exception;



public class ServiceDataNotFoundException extends RuntimeException {

    public ServiceDataNotFoundException(String message) {
        super(message);
    }


    public ServiceDataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }



}
