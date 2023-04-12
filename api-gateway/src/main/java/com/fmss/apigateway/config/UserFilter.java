package com.fmss.apigateway.config;

import com.fmss.commondata.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class UserFilter implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenValidateInterceptor(jwtUtil())).addPathPatterns("/**");
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}
