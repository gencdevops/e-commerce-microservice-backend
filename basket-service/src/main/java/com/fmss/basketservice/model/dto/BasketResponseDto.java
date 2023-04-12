package com.fmss.basketservice.model.dto;

import com.fmss.basketservice.model.enitity.BasketItem;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record BasketResponseDto(List<BasketItemResponseDto> basketItemList, BigDecimal totalPrice) {
}
