package com.fmss.orderservice.dto;

import com.fmss.commondata.dtos.response.BasketResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PlaceOrderRequestDTO(
        @NotNull
        UUID userId,

        @NotNull
        BasketResponseDto basketResponseDto) {
}
