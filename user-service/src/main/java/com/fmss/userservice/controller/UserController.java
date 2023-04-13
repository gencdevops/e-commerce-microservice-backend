package com.fmss.userservice.controller;

import com.fmss.userservice.model.dto.request.UserRegisterRequestDto;
import com.fmss.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        userService.registerUser(userRegisterRequestDto);
        return new ResponseEntity<>("User successfully registered", HttpStatus.OK);
    }

    @GetMapping("/forgot-password/{email}")
    public ResponseEntity sendForgotPasswordLink(@PathVariable("email") String email) {
        userService.sendForgotPasswordMail(email);
        return ResponseEntity.ok().build();
    }
}
