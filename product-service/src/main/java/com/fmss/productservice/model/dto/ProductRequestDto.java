package com.fmss.productservice.model.dto;

import java.math.BigDecimal;

public record ProductRequestDto(String name, BigDecimal price, boolean status) {
}
