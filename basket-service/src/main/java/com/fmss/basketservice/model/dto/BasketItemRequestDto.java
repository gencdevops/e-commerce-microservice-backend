package com.fmss.basketservice.model.dto;

import java.util.UUID;

public record BasketItemRequestDto(
        UUID productId,
        Integer quantity,
        UUID basketId)
{}
