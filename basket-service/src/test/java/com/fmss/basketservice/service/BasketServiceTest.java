//package com.fmss.basketservice.service;
//
//import com.fmss.basketservice.mapper.BasketItemMapper;
//import com.fmss.basketservice.mapper.BasketMapper;
//import com.fmss.basketservice.model.entity.Basket;
//import com.fmss.basketservice.model.enums.BasketStatus;
//import com.fmss.basketservice.repository.BasketRepository;
//import com.fmss.commondata.dtos.response.BasketResponseDto;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class BasketServiceTest {
//
//    @Mock
//    BasketRepository basketRepository;
//
//    @Mock
//    BasketMapper basketMapper;
//
//    @Mock
//    BasketItemMapper basketItemMapper;
//
//    @InjectMocks
//    BasketService basketService;
//
//    @Test
//    void getBasketByUserId_Should_ReturnCurrentBasketIfExists() {
//        UUID userId = UUID.fromString("test");
//
//        Basket basket = new Basket(Collections.emptyList(), BigDecimal.ONE, BasketStatus.ACTIVE, userId);
//        basket.setId(UUID.fromString("basketTestId"));
//
//        when(basketRepository.findActiveBasketByUserId(userId)).thenReturn(Optional.of(basket));
//        when(basketMapper.basketToBasketResponseDto(basket)).thenReturn(new BasketResponseDto(Collections.emptyList(), basket.getTotalPrice(), basket.getId()));
//
//        BasketResponseDto basketByUserId = basketService.getBasketByUserId(userId);
//
//        assertAll(
//                () -> assertEquals(basket.getBasketItems(), basketByUserId.basketItemList()),
//                () -> assertEquals(basket.getTotalPrice(), basketByUserId.totalPrice()),
//                () -> assertEquals(basket.getId(), basketByUserId.basketId())
//        );
//    }
//
//    @Test
//    void disableBasket() {
//        UUID basketId = UUID.fromString("test");
//
//        Basket basket = spy(new Basket(Collections.emptyList(), BigDecimal.ONE, BasketStatus.ACTIVE, basketId));
//        basket.setId(UUID.fromString("basketTestId"));
//
//        when(basketRepository.findById(basketId)).thenReturn(Optional.of(basket));
//
//        basketService.disableBasket(basketId);
//
//        verify(basket).setBasketStatus(BasketStatus.PASSIVE);
//        verify(basketRepository).save(basket);
//    }
//
//}