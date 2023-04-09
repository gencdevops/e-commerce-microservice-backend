package com.fmss.productservice.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductResponseDto(String name, BigDecimal price) implements Serializable {
}
