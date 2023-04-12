package com.fmss.basketservice.mapper;

import com.fmss.basketservice.model.dto.BasketResponseDto;
import com.fmss.basketservice.model.enitity.Basket;
import org.springframework.stereotype.Component;

@Component
public class BasketMapper {

    BasketItemMapper basketItemMapper;

    public BasketResponseDto basketToBasketResponseDto(Basket basket){
        return BasketResponseDto.builder()
                .basketItemList(basketItemMapper.basketItemListToBasketItemResponseDtoList(basket.getBasketItems()))
                .totalPrice(basket.getTotalPrice())
                .build();
    }
}
