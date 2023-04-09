package com.fmss.productservice.service;

import com.fmss.productservice.exception.ProductNotFoundException;
import com.fmss.productservice.mapper.ProductMapper;
import com.fmss.productservice.model.Product;
import com.fmss.productservice.model.dto.ProductRequestDto;
import com.fmss.productservice.model.dto.ProductResponseDto;
import com.fmss.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Cacheable(value = "allProducts")
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.getAllProducts().parallelStream().map(productMapper::toResponseDto).toList();
    }

    @CacheEvict(value = "allProducts")
    public ProductResponseDto updateProduct(ProductRequestDto productRequestDto, UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        product.setName(productRequestDto.name());
        product.setPrice(productRequestDto.price());
        product.setStatus(productRequestDto.status());

        return productMapper.toResponseDto(productRepository.save(product));
    }


    public void deleteProduct(UUID productId) {
        productRepository.findById(productId)
                .ifPresentOrElse(
                        product -> productRepository.delete(product),
                        () -> {
                            throw new ProductNotFoundException();
                        });
    }

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto){
        Product createdProduct = productRepository.save(productMapper.toEntity(productRequestDto));
        return productMapper.toResponseDto(createdProduct);
    }
}
