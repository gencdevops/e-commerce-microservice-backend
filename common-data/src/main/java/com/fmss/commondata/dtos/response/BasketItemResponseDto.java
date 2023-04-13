package com.fmss.commondata.dtos.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record BasketItemResponseDto(
        UUID productId,

        Integer quantity,

        UUID basketItemId,

        String name,

        String imgUrl,

        BigDecimal price) {
}
