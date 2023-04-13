package com.fmss.basketservice.service;

import com.fmss.basketservice.exception.BasketNotFoundException;
import com.fmss.basketservice.feign.ProductClient;
import com.fmss.basketservice.mapper.BasketItemMapper;
import com.fmss.basketservice.mapper.BasketMapper;
import com.fmss.basketservice.model.dto.BasketItemRequestDto;
import com.fmss.basketservice.model.dto.BasketItemUpdateDto;
import com.fmss.commondata.dtos.response.BasketItemResponseDto;
import com.fmss.commondata.dtos.response.BasketResponseDto;
import com.fmss.basketservice.model.entity.Basket;
import com.fmss.basketservice.model.entity.BasketItem;
import com.fmss.basketservice.model.enums.BasketStatus;
import com.fmss.basketservice.repository.BasketItemRepository;
import com.fmss.basketservice.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final BasketMapper basketMapper;
    private final BasketItemMapper basketItemMapper;
    private final ProductClient productClient;

    private Basket createBasket (UUID userId) {
        Basket newBasket = Basket.builder()
                .userId(userId)
                .basketStatus(BasketStatus.ACTIVE)
                .totalPrice(BigDecimal.ZERO)
                .basketItems(new ArrayList<>())
                .build();

        return basketRepository.save(newBasket);
    }

    public BasketResponseDto getBasketByUserId(UUID userId) {
        Basket basketByUserId = basketRepository.findActiveBasketByUserId(userId).orElse(createBasket(userId));
        BasketResponseDto basketResponseDto = basketMapper.toResponseDto(basketByUserId);

        return basketResponseDto;
    }

    public BasketResponseDto getBasketByBasketId(UUID basketId) {
        return basketMapper.toResponseDto(
                getById(basketId)
        );
    }

    public void disableBasket(UUID basketId){
        Basket basket = getById(basketId);
        basket.setBasketStatus(BasketStatus.PASSIVE);

        basketRepository.save(basket);
    }

    private Basket getById(UUID basketId){
        return basketRepository.findById(basketId).orElseThrow(BasketNotFoundException::new);
    }

    public void deleteBasket(UUID basketId){
        basketRepository.deleteById(basketId);
    }

    public BasketItemResponseDto addBasketItemToBasket(BasketItemRequestDto basketItemRequestDto){
        BasketItem basketItem = basketItemMapper.toEntity(basketItemRequestDto);

        basketItemRepository.save(basketItem);

        return basketItemMapper.toResponseDto(basketItem);
    }

    public void deleteBasketItemFromBasket(UUID basketItemId){
        basketItemRepository.deleteById(basketItemId);
    }

    public BasketItemResponseDto updateQuantityBasketItem(BasketItemUpdateDto basketItemUpdateDto){
        BasketItem basketItem = basketItemRepository.findById(basketItemUpdateDto.basketItemId()).orElseThrow(() -> new RuntimeException("Basket item not found."));

        basketItem.setQuantity(basketItemUpdateDto.quantity());

        return basketItemMapper.toResponseDto(basketItemRepository.save(basketItem));
    }

}
