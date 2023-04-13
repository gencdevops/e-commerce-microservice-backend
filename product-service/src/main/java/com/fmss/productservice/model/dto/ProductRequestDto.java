package com.fmss.productservice.model.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
@RedisHash("PRODUCT-LIST")
@TypeAlias("PRODUCT-LIST")
public class ProductRequestDto implements Serializable {
    private String name;
    private BigDecimal price;
    private Boolean status;
    private String fileName;
}
