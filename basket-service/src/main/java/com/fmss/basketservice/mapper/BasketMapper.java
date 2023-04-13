package com.fmss.basketservice.mapper;


import com.fmss.basketservice.model.entity.Basket;
import com.fmss.commondata.dtos.response.BasketResponseDto;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(implementationName = "BasketMapperImpl", componentModel = "spring", imports = {Basket.class})
public abstract class BasketMapper {

    @Autowired
    public BasketItemMapper basketItemMapper;
    
    @Mapping(target = "basketItemList", expression = "java(basketItemMapper.toResponseDtoList(basket.getBasketItems()))")
    public abstract BasketResponseDto toResponseDto(Basket basket);
}
