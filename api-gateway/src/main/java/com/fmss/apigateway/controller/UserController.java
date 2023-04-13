package com.fmss.apigateway.controller;

import com.fmss.apigateway.service.UserService;
import com.fmss.commondata.dtos.request.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @PostMapping(API_USER_REGISTER)
    @ResponseStatus(HttpStatus.CREATED)
    public String saveUser(@RequestBody UserRegisterRequestDto userRegisterRequestDto) {

        userService.registerUser(userRegisterRequestDto);
    }

    @Operation(summary = "Create order")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "201",
            description = "Place order",
            content = @Content(
                    mediaType = "application/json")))
    @PostMapping(API_USER_VALIDATE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public boolean validateToken(@RequestBody UserDetails userDetails, @PathVariable String token) {
        return userService.validateToken(token, userDetails);
    }

    @GetMapping(API_USER_FORGET)
    @ResponseStatus(HttpStatus.OK)
    public void sendForgetPasswordLink(@PathVariable String email) {
        userService.sendForgotPasswordMail(email);
    }
}






