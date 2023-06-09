package com.fmss.basketservice.mapper;


import com.fmss.basketservice.model.entity.Basket;
import com.fmss.commondata.dtos.response.BasketItemResponseDto;
import com.fmss.commondata.dtos.response.BasketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

//@Mapper(implementationName = "BasketMapperImpl", componentModel = "spring", imports = {Basket.class})
//public abstract class BasketMapper {
//
//    @Autowired
//    public BasketItemMapper basketItemMapper;
//
//    @Mapping(target = "basketItemList", expression = "java(basketItemMapper.toResponseDtoList(basket.getBasketItems()))")
//    @Mapping(target = "totalPrice", expression = "java(basketItemMapper.toResponseDtoList(basket.getBasketItems()).map(BasketItemResponseDto::price).reduce(BigDecimal::add).get())")
//    public abstract BasketResponseDto toResponseDto(Basket basket);
//}

@Component
@RequiredArgsConstructor
public class BasketMapper {
    private final BasketItemMapper basketItemMapper;

    public BasketResponseDto toResponseDto(Basket basket){
        List<BasketItemResponseDto> basketItemResponseDtos = basketItemMapper.toResponseDtoList(basket.getBasketItems());

        return BasketResponseDto.builder()
                .basketId(basket.getBasketId())
                .basketItemList(basketItemResponseDtos.stream().sorted(Comparator.comparing(BasketItemResponseDto::name)).toList())
                .totalPrice(
                        basketItemResponseDtos
                                .stream()
                                .map(basketItemResponseDto -> basketItemResponseDto.price().multiply(BigDecimal.valueOf(basketItemResponseDto.quantity())))
                                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
                .build();
    }

}
