package com.fmss.basketservice.service;

import com.fmss.basketservice.exception.BasketNotFoundException;
import com.fmss.basketservice.mapper.BasketItemMapper;
import com.fmss.basketservice.mapper.BasketMapper;
import com.fmss.basketservice.model.dto.BasketItemRequestDto;
import com.fmss.basketservice.model.dto.BasketItemResponseDto;
import com.fmss.basketservice.model.dto.BasketResponseDto;
import com.fmss.basketservice.model.entity.Basket;
import com.fmss.basketservice.model.entity.BasketItem;
import com.fmss.basketservice.model.enums.BasketStatus;
import com.fmss.basketservice.repository.BasketItemRepository;
import com.fmss.basketservice.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final BasketMapper basketMapper;
    private final BasketItemMapper basketItemMapper;

    private Basket createBasket (String userId) {
        Basket newBasket = Basket.builder()
                .userId(userId)
                .basketStatus(BasketStatus.ACTIVE)
                .totalPrice(BigDecimal.ZERO)
                .basketItems(new ArrayList<>())
                .build();

        return basketRepository.save(newBasket);
    }

    public BasketResponseDto getBasketByUserId(String userId) {
        Basket basketByUserId = basketRepository.findActiveBasketByUserId(userId).orElse(createBasket(userId));
        BasketResponseDto basketResponseDto = basketMapper.basketToBasketResponseDto(basketByUserId);

        return basketResponseDto;
    }

    public BasketResponseDto getBasketByBasketId(String basketId) {
        return basketMapper.basketToBasketResponseDto(
                getById(basketId)
        );
    }

    public void disableBasket(String basketId){
        Basket basket = getById(basketId);
        basket.setBasketStatus(BasketStatus.PASSIVE);

        basketRepository.save(basket);
    }

    private Basket getById(String basketId){
        return basketRepository.findById(basketId).orElseThrow(BasketNotFoundException::new);
    }

    public void deleteBasket(String basketId){
        basketRepository.deleteById(basketId);
    }

    public BasketItemResponseDto addBasketItemToBasket(BasketItemRequestDto basketItemRequestDto){
        Basket basket = getById(basketItemRequestDto.basketId());

        BasketItem basketItem = new BasketItem(basketItemRequestDto.basketId(), basketItemRequestDto.quantity(), basket);

        basketItemRepository.save(basketItem);

        return basketItemMapper.toResponseDto(basketItem);
    }

    public void deleteBasketItemFromBasket(String basketItemId){
        basketItemRepository.deleteById(UUID.fromString(basketItemId));
    }

}
