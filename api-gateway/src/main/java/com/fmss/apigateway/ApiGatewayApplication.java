package com.fmss.apigateway;

import com.fmss.commondata.model.LoginRequestDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ApiGatewayApplication {

    public static void main(String[] args) {

        LoginRequestDto asd;
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
