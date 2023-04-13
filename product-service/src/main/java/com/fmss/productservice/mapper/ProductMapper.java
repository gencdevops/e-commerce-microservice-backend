package com.fmss.productservice.mapper;

import com.fmss.productservice.model.Product;
import com.fmss.productservice.model.dto.ProductRequestDto;
import com.fmss.productservice.model.dto.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(implementationName = "PaymentMapperImpl", componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "image", source = "url")
    ProductResponseDto toProductResponseDto(Product product);

    Product toProduct(ProductRequestDto productRequestDto, String url);
}
