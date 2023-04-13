package com.fmss.commondata.dtos.request;

import java.util.UUID;

public record LoginRequestDto(
        UUID username,

        UUID password) {
}
