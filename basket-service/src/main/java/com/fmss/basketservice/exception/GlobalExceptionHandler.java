package com.fmss.basketservice.exception;


import com.fmss.commondata.model.ErrorBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BasketNotFoundException.class)
    public ResponseEntity<ErrorBody> catchBasketNotFoundExceptions(BasketNotFoundException basketNotFoundException){
        return ResponseEntity.ok(ErrorBody.builder().errorCode(404).errorDescription(basketNotFoundException.getMessage()).build());
    }

}
