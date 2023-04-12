package com.fmss.basketservice.model.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record BasketResponseDto(List<BasketItemResponseDto> basketItemList, BigDecimal totalPrice, String basketId) {
}
