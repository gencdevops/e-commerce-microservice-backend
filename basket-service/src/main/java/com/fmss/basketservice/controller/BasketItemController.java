package com.fmss.basketservice.controller;


import com.fmss.basketservice.model.dto.BasketItemRequestDto;
import com.fmss.basketservice.model.dto.BasketItemUpdateDto;
import com.fmss.basketservice.service.BasketService;
import com.fmss.commondata.dtos.response.BasketItemResponseDto;
import com.fmss.commondata.dtos.response.BasketResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.fmss.basketservice.constants.BasketConstants.*;

@RestController
@RequestMapping(API_PREFIX + API_VERSION_V1 + API_BASKET_ITEMS)
@RequiredArgsConstructor
@CrossOrigin
public class BasketItemController {

    private final BasketService basketService;

    @Operation(summary = "Add basket item to basket")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "200",
            description = "Add basket item to basket",
            content = @Content(
                    schema = @Schema(implementation = BasketItemResponseDto.class),
                    mediaType = "application/json")))
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public BasketItemResponseDto addBasketItemToBasket(@RequestBody BasketItemRequestDto basketItemRequestDto) {
        return basketService.addBasketItemToBasket(basketItemRequestDto);
    }

    @Operation(summary = "Update Basket Item ")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "200",
            description = "Update basket item",
            content = @Content(
                    schema = @Schema(implementation = BasketResponseDto.class),
                    mediaType = "application/json")))
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/quantity-increment/{basketItemId}")
    public BasketResponseDto updateQuantityBasketItem(@RequestBody BasketItemUpdateDto basketItemUpdateDto){
        return basketService.updateQuantityBasketItem(basketItemUpdateDto);
    }

    @Operation(summary = "Delete basket item ")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "204",
            description = "Delete basket item",
            content = @Content(
                    schema = @Schema(implementation = BasketResponseDto.class),
                    mediaType = "application/json")))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{basketItemId}")
    public BasketResponseDto deleteBasketItemOnBasket(@PathVariable UUID basketItemId) {
        return basketService.deleteBasketItemFromBasket(basketItemId);
    }

    @Operation(summary = "Delete all basket item ")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "204",
            description = "Delete all basket item",
            content = @Content(
                    mediaType = "application/json")))
    @DeleteMapping("/all/{basketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllBasketItems(@PathVariable UUID basketId) {
        basketService.deleteAllBasketItems(basketId);
    }

}
