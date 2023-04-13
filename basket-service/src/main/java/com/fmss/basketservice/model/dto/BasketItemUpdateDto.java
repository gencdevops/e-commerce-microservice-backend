package com.fmss.basketservice.model.dto;


import java.util.UUID;

public record BasketItemUpdateDto(UUID basketItemId, Integer quantity) {}
