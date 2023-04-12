package com.fmss.apigateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebApplicationConfiguration implements WebMvcConfigurer {

    private final TokenValidateInterceptor tokenValidateInterceptor;

    public WebApplicationConfiguration(TokenValidateInterceptor tokenValidateInterceptor) {
        this.tokenValidateInterceptor = tokenValidateInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(tokenValidateInterceptor).addPathPatterns("/**");
    }
}
