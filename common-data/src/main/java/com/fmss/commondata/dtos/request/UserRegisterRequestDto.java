package com.fmss.commondata.dtos.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record UserRegisterRequestDto(
        @NotNull(message = "Firstname must not be null")
        String firstName,
        String lastName,
        String userName,
        String email,
        @Size(max = 128)
        String password

) {}
