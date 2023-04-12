package com.fmss.basketservice.service;

import com.fmss.basketservice.mapper.BasketItemMapper;
import com.fmss.basketservice.mapper.BasketMapper;
import com.fmss.basketservice.model.dto.BasketResponseDto;
import com.fmss.basketservice.model.entity.Basket;
import com.fmss.basketservice.model.enums.BasketStatus;
import com.fmss.basketservice.repository.BasketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

        Basket basket = new Basket(Collections.emptyList(), BigDecimal.ONE, BasketStatus.ACTIVE, "test");
        basket.setId("basketTestId");

        when(basketRepository.findActiveBasketByUserId(userId)).thenReturn(Optional.of(basket));
        when(basketMapper.basketToBasketResponseDto(basket)).thenReturn(new BasketResponseDto(Collections.emptyList(), basket.getTotalPrice(), basket.getId()));

        BasketResponseDto basketByUserId = basketService.getBasketByUserId(userId);

        assertAll(
                () -> assertEquals(basket.getBasketItems(), basketByUserId.basketItemList()),
                () -> assertEquals(basket.getTotalPrice(), basketByUserId.totalPrice()),
                () -> assertEquals(basket.getId(), basketByUserId.basketId())
        );
    }

    @Test
    void disableBasket() {
        String basketId = "test";

        Basket basket = spy(new Basket(Collections.emptyList(), BigDecimal.ONE, BasketStatus.ACTIVE, "test"));
        basket.setId("basketTestId");

        when(basketRepository.findById(basketId)).thenReturn(Optional.of(basket));

        basketService.disableBasket(basketId);

        verify(basket).setBasketStatus(BasketStatus.PASSIVE);
        verify(basketRepository).save(basket);
    }

}