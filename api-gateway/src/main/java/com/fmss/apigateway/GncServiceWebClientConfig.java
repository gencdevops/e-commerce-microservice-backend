package com.fmss.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GncServiceWebClientConfig {

    @Value(value = "${dc.gnc.service.url}")
    private String gncServiceUrl;

    @LoadBalanced
    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean(name = "GncServiceWebClient")
    WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(gncServiceUrl).build();
    }

}