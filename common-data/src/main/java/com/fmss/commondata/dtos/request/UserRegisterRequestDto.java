package com.fmss.commondata.dtos.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record UserRegisterRequestDto(
        @NotNull(message = "Firstname must not be null")
        String firstName,
        @NotNull(message = "Lastname must not be null")
        String lastName,
        @NotNull(message = "Username must not be null")
        String userName,
        @NotNull(message = "Email must not be null")
        String email,
        @Size(max = 128)
        @NotNull(message = "Password must not be null")
        String password
) {}
