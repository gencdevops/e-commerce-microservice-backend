package com.fmss.apigateway.service;

import com.fmss.commondata.model.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final WebClient userServiceWebClient;

    public Mono<ResponseEntity<String>> login(LoginRequestDto loginRequestDto){
        return userServiceWebClient
                .post()
                .uri("/login")
                .bodyValue(loginRequestDto)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok().header("token", response).body("Success"));
    }
}
