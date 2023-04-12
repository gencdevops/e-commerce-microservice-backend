package com.fmss.basketservice.mapper;

import com.fmss.basketservice.model.dto.BasketItemResponseDto;
import com.fmss.basketservice.model.enitity.BasketItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasketItemMapper {

    public BasketItemResponseDto basketItemToBasketItemResponseDto(BasketItem basketItem) {
        return BasketItemResponseDto.builder()
                .productId(basketItem.getProductId())
                .quantity(basketItem.getQuantity())
                .build();
    }

    public List<BasketItemResponseDto> basketItemListToBasketItemResponseDtoList(List<BasketItem> basketItemList) {
        return basketItemList.stream()
                .map(basketItem -> basketItemToBasketItemResponseDto(basketItem))
                .toList();
    }
}
