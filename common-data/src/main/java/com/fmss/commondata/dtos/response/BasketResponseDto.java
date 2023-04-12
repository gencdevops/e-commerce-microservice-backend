package com.fmss.commondata.dtos.response;

import com.fmss.commondata.dtos.response.BasketItemResponseDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record BasketResponseDto(List<BasketItemResponseDto> basketItemList, BigDecimal totalPrice, UUID basketId) {
}
