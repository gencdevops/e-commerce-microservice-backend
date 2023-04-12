package com.fmss.basketservice.model.dto;

import lombok.Builder;

@Builder
public record BasketItemResponseDto(String productId, Integer quantity, String basketItemId) {
}
