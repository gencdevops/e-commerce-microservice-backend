package com.fmss.orderservice.dto;

import com.fmss.commondata.dtos.response.BasketResponseDto;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PlaceOrderRequestDTO(
        @NotNull
        UUID userId,
        @NotNull
        BasketResponseDto basketResponseDto) {
}
