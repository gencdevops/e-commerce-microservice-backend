package com.fmss.basketservice.model.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record BasketItemResponseDto(UUID productId, Integer quantity, UUID basketItemId, String name, String imgUrl) {
}
