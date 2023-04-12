package com.fmss.apigateway.controller;

import com.fmss.apigateway.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
//    @PostMapping("/login")
//    public Mono<ResponseEntity<String>> login(@RequestBody LoginRequestDto loginRequestDto) {
//        return userService.login(loginRequestDto);
//    }

    @GetMapping
    public Mono<ResponseEntity<String>> hello() {
        log.info("Hello");
        return Mono.just(ResponseEntity.ok("HELLO"));
    }
}
