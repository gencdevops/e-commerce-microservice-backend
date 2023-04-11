package com.fmss.basketservice.controller;

import com.fmss.basketservice.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;


    @PostMapping
    public void createBasket(String userId) {
        basketService.createBasket(userId);
    }

    @GetMapping("/userid/{userId}")
    public void getBasketByUserId(@PathVariable String userId) {

    }

    @GetMapping("/basketId/{basketId}")
    public void getBasketByBasketId(@PathVariable String basketId) {

    }
//
//    @PutMapping
//    public void disableBasket() {
//
//    }
//
//    @DeleteMapping
//    public void deleteBasket() {
//
//    }
//
//    @PostMapping
//    public void addBasketItemToBasket(BasketItem basketItem) {
//
//    }
//
//    @PostMapping
//    public void deleteBasketItemOnBasket(BasketItem basketItem) {
//
//    }
}
