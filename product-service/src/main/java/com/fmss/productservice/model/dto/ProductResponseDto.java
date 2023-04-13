package com.fmss.productservice.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDto(
        UUID productId,
        String name,
        String image,
        BigDecimal price) implements Serializable {
}
