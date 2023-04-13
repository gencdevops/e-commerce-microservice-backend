package com.fmss.userservice.controller;

import com.fmss.userservice.request.UserRegisterRequestDto;
import com.fmss.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.fmss.userservice.constants.UserConstants.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = API_PREFIX + API_VERSION_V1 + API_USER)
public class UserController {

    private final UserService userService;


    @PostMapping(API_USER_REGISTER)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveUser(@RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        log.info("save user method entry :{}", userRegisterRequestDto.userName());
        userService.registerUser(userRegisterRequestDto);
    }


    @Operation(summary = "Validate token")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "201",
            description = "Validate token",
            content = @Content(
                    mediaType = "application/json")))
    @PostMapping(API_USER_VALIDATE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public boolean validateToken(@RequestBody UserDetails userDetails, @PathVariable String token) {
        boolean validateToken = userService.validateToken(token, userDetails);
        log.info("is token expire :{}", validateToken);
        return validateToken;
    }

    @Operation(summary = "Send forget password link token")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "201",
            description = "Send forget password link token",
            content = @Content(
                    mediaType = "application/json")))
    @GetMapping(API_USER_FORGET)
    @ResponseStatus(HttpStatus.OK)
    public void sendForgetPasswordLink(@PathVariable String email) {
        userService.sendForgotPasswordMail(email);
    }
}
