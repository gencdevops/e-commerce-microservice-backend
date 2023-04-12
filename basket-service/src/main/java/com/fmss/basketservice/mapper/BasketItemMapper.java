package com.fmss.basketservice.mapper;

import com.fmss.basketservice.model.dto.BasketItemResponseDto;
import com.fmss.basketservice.model.entity.BasketItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasketItemMapper {

    public BasketItemResponseDto toResponseDto(BasketItem basketItem) {
        return BasketItemResponseDto.builder()
                .basketItemId(basketItem.getId())
                .productId(basketItem.getProductId())
                .quantity(basketItem.getQuantity())
                .build();
    }

    public List<BasketItemResponseDto> toResponseDtoList(List<BasketItem> basketItemList) {
        return basketItemList.stream()
                .map(this::toResponseDto)
                .toList();
    }
}
