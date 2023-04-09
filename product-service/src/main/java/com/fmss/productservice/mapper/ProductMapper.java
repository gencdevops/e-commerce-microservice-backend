package com.fmss.productservice.mapper;

import com.fmss.productservice.model.Product;
import com.fmss.productservice.model.dto.ProductRequestDto;
import com.fmss.productservice.model.dto.ProductResponseDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductMapper {
    public ProductResponseDto toResponseDto(Product product){
        String name;
        BigDecimal price;

        name = product.getName();
        price = product.getPrice();

        return new ProductResponseDto(name, price);
    }

    public Product toEntity(ProductRequestDto productRequestDto){
        return new Product(productRequestDto.name(), productRequestDto.price(), productRequestDto.status());
    }
}
