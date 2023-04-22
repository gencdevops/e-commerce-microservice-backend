package com.fmss.commondata.dtos.request;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record LoginRequestDto(
        @NotNull(message = "Username must not be null")
        UUID username,
        @NotNull(message = "Password must not be null")
        UUID password) {
}
