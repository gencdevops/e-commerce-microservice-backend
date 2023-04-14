package com.fmss.basketservice.mapper;


import com.fmss.basketservice.exception.BasketNotFoundException;
import com.fmss.basketservice.feign.ProductClient;
import com.fmss.basketservice.model.dto.BasketItemRequestDto;
import com.fmss.basketservice.model.dto.BasketItemRequestWithUserIdDto;
import com.fmss.basketservice.model.dto.ProductResponseDto;
import com.fmss.basketservice.model.entity.BasketItem;
import com.fmss.basketservice.repository.BasketItemRepository;
import com.fmss.basketservice.repository.BasketRepository;
import com.fmss.commondata.dtos.response.BasketItemResponseDto;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

//@Mapper(componentModel = "spring")
//public interface BasketItemMapper {
//
//    BasketItemResponseDto toResponseDto(BasketItem basketItem);
//
//    List<BasketItemResponseDto> toResponseDtoList(List<BasketItem> basketItemList);
//
//    BasketItem toEntity(BasketItemRequestDto basketItemRequestDto);
//}

@Component
@RequiredArgsConstructor
public class BasketItemMapper {
    private final BasketRepository basketRepository;
    private final ProductClient productClient;

    private final BasketItemRepository basketItemRepository;

    public BasketItemResponseDto toResponseDto(BasketItem basketItem){
        ProductResponseDto productById = productClient.getProductById(basketItem.getProductId());

        return BasketItemResponseDto.builder()
                .basketItemId(basketItem.getBasketItemId())
                .price(productById.price())
                .imgUrl(productById.image())
                .name(productById.name())
                .productId(basketItem.getProductId())
                .quantity(basketItem.getQuantity())
                .build();
    }

    public BasketItem toEntity(BasketItemRequestDto basketItemRequestDto){
        return BasketItem.builder()
                .productId(basketItemRequestDto.productId())
                .quantity(basketItemRequestDto.quantity())
                .basket(basketRepository.findById(basketItemRequestDto.basketId()).orElseThrow(BasketNotFoundException::new))
                .build();
    }

    public BasketItem toEntityWithUserId(BasketItemRequestWithUserIdDto basketItemRequestWithUserIdDto){
        return BasketItem.builder()
                .productId(basketItemRequestWithUserIdDto.productId())
                .quantity(basketItemRequestWithUserIdDto.quantity())
                .basket(basketRepository.findActiveBasketByUserId(basketItemRequestWithUserIdDto.userId()).orElseThrow(BasketNotFoundException::new))
                .build();
    }

    public List<BasketItemResponseDto> toResponseDtoList(List<BasketItem> basketItems){
        return basketItems.stream().map(this::toResponseDto).toList();
    }
}
