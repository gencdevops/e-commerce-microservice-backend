package com.fmss.apigateway.exception;

public class CrackException extends RuntimeException {

   public CrackException(String message) {
       super(message);
   }

    public CrackException(String message, Throwable cause) {
        super(message, cause);
    }
}