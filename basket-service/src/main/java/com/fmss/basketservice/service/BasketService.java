package com.fmss.basketservice.service;

import com.fmss.basketservice.mapper.BasketMapper;
import com.fmss.basketservice.model.dto.BasketResponseDto;
import com.fmss.basketservice.model.enitity.Basket;
import com.fmss.basketservice.model.enums.BasketStatus;
import com.fmss.basketservice.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final BasketMapper basketMapper;
    public BasketResponseDto createBasket (String userId) {
        Basket newBasket = Basket.builder()
                .userId(userId)
                .basketStatus(BasketStatus.ACTIVE)
                .totalPrice(BigDecimal.ZERO)
                .basketItems(new ArrayList<>())
                .build();
        basketRepository.save(newBasket);
        return basketMapper.basketToBasketResponseDto(newBasket);
    }

    public BasketResponseDto getBasketByUserId (String userId) {
        List<Basket> basketList = basketRepository.findBasketsByUserId(userId);

        if (basketList.isEmpty()) {
            return createBasket(userId);
        }

        if (basketList.stream().filter(basket -> basket.getBasketStatus() == BasketStatus.ACTIVE)) {

        }

    }

    private

    public void getBasketByBasketId () {

    }

}
