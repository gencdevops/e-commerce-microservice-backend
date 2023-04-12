package com.fmss.orderservice.dto;

import com.fmss.commondata.dtos.response.BasketResponseDto;
import jakarta.validation.constraints.NotNull;

public record PlaceOrderRequestDTO(
        @NotNull
        String userId,
        @NotNull
        BasketResponseDto basketResponseDto) {
}
