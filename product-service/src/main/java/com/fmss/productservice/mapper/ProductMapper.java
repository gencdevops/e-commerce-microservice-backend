package com.fmss.productservice.mapper;

import com.fmss.productservice.model.Product;
import com.fmss.productservice.model.dto.ProductRequestDto;
import com.fmss.productservice.model.dto.ProductResponseDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ProductMapper {
    public ProductResponseDto toResponseDto(Product product) {
        
        String id;
        String name;
        String image;
        BigDecimal price;

        id = String.valueOf(product.getProductId());
        name = product.getName();
        image = product.getUrl();
        price = product.getPrice();

        return new ProductResponseDto(id, name, image, price);
    }

    public Product toEntity(ProductRequestDto productRequestDto, String url) {
        return new Product(UUID.randomUUID(), productRequestDto.getName(), productRequestDto.getPrice(), productRequestDto.getStatus(), url, LocalDateTime.now(), LocalDateTime.now());
    }
}
