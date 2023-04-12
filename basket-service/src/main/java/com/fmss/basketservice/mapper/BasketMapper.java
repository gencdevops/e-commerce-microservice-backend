package com.fmss.basketservice.mapper;

import com.fmss.basketservice.model.dto.BasketResponseDto;
import com.fmss.basketservice.model.entity.Basket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;

@Component
@RequiredArgsConstructor
public class BasketMapper {

    private final BasketItemMapper basketItemMapper;

    public BasketResponseDto basketToBasketResponseDto(Basket basket){
        return BasketResponseDto.builder()
                .basketId(basket.getId())
                .basketItemList(basketItemMapper.toResponseDtoList(basket.getBasketItems()))
                .totalPrice(basket.getTotalPrice())
                .build();
    }
}
