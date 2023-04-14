package com.fmss.orderservice.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebApplicationConfiguration implements WebMvcConfigurer {

    private final TokenValidateConfiguration tokenValidateInterceptor;

    public WebApplicationConfiguration(TokenValidateConfiguration tokenValidateInterceptor) {
        this.tokenValidateInterceptor = tokenValidateInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(tokenValidateInterceptor).addPathPatterns("/**");
    }
}
