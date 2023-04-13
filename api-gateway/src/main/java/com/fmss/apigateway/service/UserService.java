package com.fmss.apigateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final WebClient userServiceWebClient;

    public Mono<Boolean> isValidate(String token, String userName) {
        return userServiceWebClient
                .post()
                .uri("/validate-token/" + token)
                .bodyValue(userName)
                .retrieve()
                .toEntity(Boolean.class).hasElement();
    }
}
