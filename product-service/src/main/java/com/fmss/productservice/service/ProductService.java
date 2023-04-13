package com.fmss.productservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;


import com.fmss.productservice.configuration.RedisCacheService;
import com.fmss.productservice.exception.ProductCouldNotCreateException;
import com.fmss.productservice.exception.ProductNotFoundException;
import com.fmss.productservice.mapper.ProductMapper;
import com.fmss.productservice.model.Product;
import com.fmss.productservice.model.dto.ProductRequestDto;
import com.fmss.productservice.model.dto.ProductResponseDto;
import com.fmss.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final FileUploadService fileUploadService;
    private final RedisCacheService redisCacheService;


    public List<ProductResponseDto> getAllProducts() {
        List<ProductResponseDto> productResponseDtos = productRepository.getAllProducts()
                .parallelStream().map(productMapper::toProductResponseDto).toList();

        productResponseDtos.forEach(addDataToCache());
        return productResponseDtos;
    }

    private Consumer<ProductResponseDto> addDataToCache() {
        return productResponseDto -> {
            Map<String, Object> cacheMap = new HashMap<>();
            cacheMap.put(productResponseDto.name(), productResponseDto);
            redisCacheService.writeListToCachePutAll("products", cacheMap);
        };
    }


    public ProductResponseDto getProductById(UUID productId) {
        return productMapper.toProductResponseDto(productRepository
                .findById(productId)
                .orElseThrow(() -> {
                    log.error("Product bulunamadı: {}.", productId);
                    return new ProductNotFoundException("Product not found");
                }));
    }

    @Transactional
    public String createProduct(String productRequest, MultipartFile multipartFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ProductRequestDto productRequestDto = objectMapper.readValue(productRequest, ProductRequestDto.class);
            String fileName = productRequestDto.getFileName() != null ? productRequestDto.getFileName() : "temp";
            assert productRequestDto.getFileName() != null;
            File convFile = File.createTempFile(fileName, "-advice");
            multipartFile.transferTo(convFile);
            String url = fileUploadService.storeFile(convFile, productRequestDto.getFileName());
            Product savedProduct = productRepository.save(productMapper.toProduct(productRequestDto, url));
            log.info("Created product {}", savedProduct.getProductId());
        } catch (IOException e) {
            log.error("Product oluştururken, dosya işlemlerinde hata gerçekleşti.\n {}", e.getMessage());
            throw new ProductCouldNotCreateException("Product oluştururken, dosya işlemlerinde hata gerçekleşti.");
        }
        return "Product saved";
    }
}
