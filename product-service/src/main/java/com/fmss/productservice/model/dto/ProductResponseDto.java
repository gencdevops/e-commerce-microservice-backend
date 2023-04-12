package com.fmss.productservice.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductResponseDto(
        String id,
        String name,
        String image,
        BigDecimal price) implements Serializable {
}
