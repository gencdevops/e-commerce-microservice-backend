package com.fmss.apigateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.dcs.gncgateway.clientdtos.response.exception.GlobalExceptionResponseDto;
import com.turkcell.dcs.gncgateway.model.response.ButtonDto;
import com.turkcell.dcs.gncgateway.model.response.EnumType;
import com.turkcell.dcs.gncgateway.model.response.PopupDto;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Configuration
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private ObjectMapper objectMapper;

    public GlobalErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {

        DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();
        if (throwable instanceof UserException) {
            serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            DataBuffer dataBuffer = null;
            try {
                dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(

                        new GlobalExceptionResponseDto(401, createErrorPopupDto())));
            } catch (JsonProcessingException e) {
                dataBuffer = bufferFactory.wrap("".getBytes());
            }
            serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
        }

        serverWebExchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        DataBuffer dataBuffer = bufferFactory.wrap("Unknown error".getBytes());
        return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    private PopupDto createErrorPopupDto() {
        ButtonDto button = new ButtonDto("DeeplinkButton1", "http://gnc/deeplink");

        ButtonDto button2 = new ButtonDto("DeeplinkButton2", "http://gnc/deeplink2");

        return new PopupDto("PopupExceptionTest", "Exception message occurred", EnumType.FAIL, button, button2);


    }



}