package com.fmss.basketservice.model.dto;

public record BasketItemRequestDto(
        String productId,
        Integer quantity,
        String basketId)
{}
