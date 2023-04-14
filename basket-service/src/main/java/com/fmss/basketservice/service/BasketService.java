package com.fmss.basketservice.service;

import com.fmss.basketservice.exception.BasketItemNotFound;
import com.fmss.basketservice.exception.BasketNotFoundException;
import com.fmss.basketservice.feign.ProductClient;
import com.fmss.basketservice.mapper.BasketItemMapper;
import com.fmss.basketservice.mapper.BasketMapper;
import com.fmss.basketservice.model.dto.BasketItemRequestDto;
import com.fmss.basketservice.model.dto.BasketItemRequestWithUserIdDto;
import com.fmss.basketservice.model.dto.BasketItemUpdateDto;
import com.fmss.basketservice.model.entity.Basket;
import com.fmss.basketservice.model.entity.BasketItem;
import com.fmss.basketservice.model.enums.BasketStatus;
import com.fmss.basketservice.repository.BasketItemRepository;
import com.fmss.basketservice.repository.BasketRepository;
import com.fmss.commondata.dtos.response.BasketItemResponseDto;
import com.fmss.commondata.dtos.response.BasketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return basketMapper.toResponseDto(getById(basketId));
    }

    public void disableBasket(UUID basketId){
        Basket basket = getById(basketId);
        basket.setBasketStatus(BasketStatus.PASSIVE);

        basketRepository.save(basket);
    }

    public void disableBasketWithUserId(UUID userId){
        Basket basket = getByUserId(userId);
        basket.setBasketStatus(BasketStatus.PASSIVE);

        basketRepository.save(basket);
    }

    private Basket getById(UUID basketId){
        return basketRepository.findById(basketId).orElseThrow(BasketNotFoundException::new);
    }

    private Basket getByUserId(UUID userId){
        return basketRepository.findActiveBasketByUserId(userId).orElseThrow(BasketNotFoundException::new);
    }

    @Transactional
    @Modifying
    public void deleteBasket(UUID basketId){
        basketRepository.deleteById(basketId);
    }

    @Transactional
    @Modifying
    public void deleteBasketByUserId(UUID userId){
        Basket basketByUserId = getByUserId(userId);
        basketRepository.delete(basketByUserId);
    }

    @Transactional
    @Modifying
    public void deleteAllBasketItems(UUID basketId){
        basketItemRepository.deleteByBasket_BasketId(basketId);
    }

    @Transactional
    @Modifying
    public void deleteAllBasketItemsWithUserId(UUID userId){
        Basket basketByUserId = getByUserId(userId);
        basketItemRepository.deleteByBasket_BasketId(basketByUserId.getBasketId());
    }

    public BasketItemResponseDto addBasketItemToBasket(BasketItemRequestDto basketItemRequestDto){
        BasketItem basketItem = basketItemMapper.toEntity(basketItemRequestDto);

        basketItemRepository.save(basketItem);

        return basketItemMapper.toResponseDto(basketItem);
    }

    public BasketItemResponseDto addBasketItemToBasketWithUserId(BasketItemRequestWithUserIdDto basketItemRequestWithUserIdDto){
        BasketItem basketItem = basketItemMapper.toEntityWithUserId(basketItemRequestWithUserIdDto);

        basketItemRepository.save(basketItem);

        return basketItemMapper.toResponseDto(basketItem);
    }

    /** Verilen id ile BasketItem nesnesini siler */
    @Transactional
    @Modifying
    public BasketResponseDto deleteBasketItemFromBasket(UUID basketItemId){
        BasketItem basketItem = basketItemRepository.findById(basketItemId).orElseThrow(BasketItemNotFound::new);
        UUID currentBasketId = basketItem.getBasket().getBasketId();

        basketItemRepository.deleteById(basketItemId);
        basketItemRepository.flush();

        return basketMapper.toResponseDto(getById(currentBasketId));
    }

    public BasketResponseDto updateQuantityBasketItem(BasketItemUpdateDto basketItemUpdateDto){
        BasketItem basketItem = basketItemRepository.findById(basketItemUpdateDto.basketItemId()).orElseThrow(() -> new RuntimeException("Basket item not found."));

        basketItem.setQuantity(basketItemUpdateDto.quantity());
        basketItem = basketItemRepository.save(basketItem);

        BasketResponseDto basketResponseDto = basketMapper.toResponseDto(basketItem.getBasket());

        return basketResponseDto;
    }

}