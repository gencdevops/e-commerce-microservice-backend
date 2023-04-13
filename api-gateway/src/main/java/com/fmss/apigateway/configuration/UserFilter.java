package com.fmss.apigateway.configuration;

import com.fmss.commondata.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class UserFilter implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenValidateInterceptor(jwtUtil(), WebClient.builder().build())).addPathPatterns("/**");
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

    @Bean
    public ExchangeFilterFunction authHeaderFilter(@Value("userId") String userId) {
        return (request, next) -> {
            ClientRequest filteredRequest = ClientRequest.from(request)
                    .header("userId", userId)
                    .build();

            return next.exchange(filteredRequest);
        };
    }

    @Bean
    public WebClient webClient(ExchangeFilterFunction authHeaderFilter) {
        return WebClient.builder()
                .filter(authHeaderFilter)
                .build();
    }
}
