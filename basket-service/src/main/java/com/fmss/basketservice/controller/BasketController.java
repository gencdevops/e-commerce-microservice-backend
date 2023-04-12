package com.fmss.basketservice.controller;

import com.fmss.basketservice.model.dto.BasketItemRequestDto;
import com.fmss.basketservice.model.dto.BasketItemResponseDto;
import com.fmss.basketservice.model.dto.BasketResponseDto;
import com.fmss.basketservice.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @GetMapping("/basket/{userId}") //endpoint doğru mu ?
    public ResponseEntity<BasketResponseDto> getBasketByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(basketService.getBasketByUserId(userId));
    }

    @GetMapping("/basket/{basketId}")
    public ResponseEntity<BasketResponseDto> getBasketByBasketId(@PathVariable String basketId) {
        return ResponseEntity.ok(basketService.getBasketByBasketId(basketId));
    }

    @PutMapping("/basket/disable/{basketId}")
    public void disableBasket(@PathVariable String basketId) {
        basketService.disableBasket(basketId);
    }

    @DeleteMapping("/{basketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBasket(@PathVariable String basketId) {
        basketService.deleteBasket(basketId);
    }

    @PostMapping("/basket-item")
    public ResponseEntity<BasketItemResponseDto> addBasketItemToBasket(@RequestBody BasketItemRequestDto basketItemRequestDto) {
        return ResponseEntity.ok(basketService.addBasketItemToBasket(basketItemRequestDto));
    }


    @DeleteMapping("/basket-item/{basketItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBasketItemOnBasket(String basketItemId) {
        basketService.deleteBasketItemFromBasket(basketItemId);
    }
}
