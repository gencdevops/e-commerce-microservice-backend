package com.fmss.productservice.model.dto;

import java.math.BigDecimal;

public record ProductResponseDto(String name, BigDecimal price) {
}
