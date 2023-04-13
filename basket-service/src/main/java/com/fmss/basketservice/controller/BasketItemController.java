package com.fmss.basketservice.controller;


import com.fmss.basketservice.model.dto.BasketItemRequestDto;
import com.fmss.basketservice.model.dto.BasketItemUpdateDto;
import com.fmss.basketservice.service.BasketService;
import com.fmss.commondata.dtos.response.BasketItemResponseDto;
import com.fmss.commondata.dtos.response.BasketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/basket-item")
@RequiredArgsConstructor
public class BasketItemController {

    private final BasketService basketService;

    @PostMapping("/basket-item")
    public ResponseEntity<BasketItemResponseDto> addBasketItemToBasket(@RequestBody BasketItemRequestDto basketItemRequestDto) {
        return ResponseEntity.ok(basketService.addBasketItemToBasket(basketItemRequestDto));
    }

    @PutMapping("/quantity-increment/{basketItemId}")
    public ResponseEntity<BasketResponseDto> updateQuantityBasketItem(@RequestBody BasketItemUpdateDto basketItemUpdateDto){
        return ResponseEntity.ok(basketService.updateQuantityBasketItem(basketItemUpdateDto));
    }

    @DeleteMapping("/basket-item/{basketItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBasketItemOnBasket(@PathVariable UUID basketItemId) {
        basketService.deleteBasketItemFromBasket(basketItemId);
    }

    @DeleteMapping("/{basketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllBasket(@PathVariable UUID basketId) {
        basketService.deleteBasket(basketId);
    }

}
