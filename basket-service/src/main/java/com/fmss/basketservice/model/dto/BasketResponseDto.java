package com.fmss.basketservice.model.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record BasketResponseDto(List<BasketItemResponseDto> basketItemList, BigDecimal totalPrice, UUID basketId) {
}
