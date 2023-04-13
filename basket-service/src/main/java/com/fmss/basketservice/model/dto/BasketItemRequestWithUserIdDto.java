package com.fmss.basketservice.model.dto;

import java.util.UUID;

public record BasketItemRequestWithUserIdDto(
        UUID productId,
        Integer quantity,
        UUID userId)
{}
