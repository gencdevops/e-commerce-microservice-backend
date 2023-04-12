package com.fmss.basketservice.controller;

import com.fmss.basketservice.model.dto.BasketResponseDto;
import com.fmss.basketservice.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @GetMapping("/basket-user/{userId}")
    public ResponseEntity<BasketResponseDto> getBasketByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(basketService.getBasketByUserId(userId));
    }

    @GetMapping("/basket-basket/{basketId}")
    public ResponseEntity<BasketResponseDto> getBasketByBasketId(@PathVariable UUID basketId) {
        return ResponseEntity.ok(basketService.getBasketByBasketId(basketId));
    }

    @PutMapping("/disable/{basketId}")
    public void disableBasket(@PathVariable UUID basketId) {
        basketService.disableBasket(basketId);
    }

    @DeleteMapping("/{basketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBasket(@PathVariable UUID basketId) {
        basketService.deleteBasket(basketId);
    }


}
