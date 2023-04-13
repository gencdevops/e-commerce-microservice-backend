package com.fmss.basketservice.service;

import com.fmss.basketservice.mapper.BasketItemMapper;
import com.fmss.basketservice.mapper.BasketMapper;
import com.fmss.basketservice.model.entity.Basket;
import com.fmss.basketservice.model.enums.BasketStatus;
import com.fmss.basketservice.repository.BasketRepository;
import com.fmss.commondata.dtos.response.BasketResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    BasketRepository basketRepository;

    @Mock
    BasketMapper basketMapper;

    @Mock
    BasketItemMapper basketItemMapper;

    @InjectMocks
    BasketService basketService;

    @Test
    void getBasketByUserId_Should_ReturnCurrentBasketIfExists() {
        String userId = "test";

        Basket basket = new Basket(Collections.emptyList(), BigDecimal.ONE, BasketStatus.ACTIVE, UUID.randomUUID());
        basket.setId(UUID.fromString("basketTestId"));

        when(basketRepository.findActiveBasketByUserId(UUID.fromString(userId))).thenReturn(Optional.of(basket));
        when(basketMapper.basketToBasketResponseDto(basket)).thenReturn(new BasketResponseDto(Collections.emptyList(), basket.getTotalPrice(), basket.getId()));

        BasketResponseDto basketByUserId = basketService.getBasketByUserId(UUID.fromString(userId));

        assertAll(
                () -> assertEquals(basket.getBasketItems(), basketByUserId.basketItemList()),
                () -> assertEquals(basket.getTotalPrice(), basketByUserId.totalPrice()),
                () -> assertEquals(basket.getId(), basketByUserId.basketId())
        );
    }

    @Test
    void disableBasket() {
        String basketId = "test";

        Basket basket = spy(new Basket(Collections.emptyList(), BigDecimal.ONE, BasketStatus.ACTIVE, UUID.fromString("test")));
        basket.setId(UUID.fromString("basketTestId"));

        when(basketRepository.findById(UUID.fromString(basketId))).thenReturn(Optional.of(basket));

        basketService.disableBasket(UUID.fromString(basketId));

        verify(basket).setBasketStatus(BasketStatus.PASSIVE);
        verify(basketRepository).save(basket);
    }

}