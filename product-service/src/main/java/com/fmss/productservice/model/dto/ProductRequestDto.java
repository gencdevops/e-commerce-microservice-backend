package com.fmss.productservice.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class ProductRequestDto implements Serializable {
    private String name;
    private BigDecimal price;
    private Boolean status;
    private String fileName;
}
