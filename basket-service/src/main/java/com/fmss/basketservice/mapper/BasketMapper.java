package com.fmss.basketservice.mapper;


import com.fmss.basketservice.model.entity.Basket;
import com.fmss.commondata.dtos.response.BasketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BasketMapper {

    private final BasketItemMapper basketItemMapper;

    public BasketResponseDto basketToBasketResponseDto(Basket basket){
        return BasketResponseDto.builder()
                .basketId(basket.id())
                .basketItemList(basketItemMapper.toResponseDtoList(basket.getBasketItems()))
                .totalPrice(basket.getTotalPrice())
                .build();
    }
}
