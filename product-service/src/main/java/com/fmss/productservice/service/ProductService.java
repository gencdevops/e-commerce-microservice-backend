package com.fmss.productservice.service;

import com.fmss.productservice.mapper.ProductMapper;
import com.fmss.productservice.model.dto.ProductResponseDto;
import com.fmss.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Cacheable(value = "allProducts")
    public List<ProductResponseDto> getAllProducts(){
        return productRepository.getAllProducts().parallelStream().map(productMapper::toResponseDto).toList();
    }
}
