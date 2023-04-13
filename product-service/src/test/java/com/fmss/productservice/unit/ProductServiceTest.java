package com.fmss.productservice.unit;

import com.fmss.productservice.configuration.RedisCacheService;
import com.fmss.productservice.exception.ProductNotFoundException;
import com.fmss.productservice.mapper.ProductMapper;
import com.fmss.productservice.model.Product;
import com.fmss.productservice.model.dto.ProductResponseDto;
import com.fmss.productservice.repository.ProductRepository;
import com.fmss.productservice.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductMapper productMapper;

    @Mock
    ProductRepository productRepository;

    @Mock
    RedisCacheService redisCacheService;

    @Test
    void getAllProducts() {
        Product product = Product.builder()
                .url("https://advivemy-images.s3.us-east-2.amazonaws.com/a3434")
                .price(BigDecimal.valueOf(1012.00))
                .name("aaa")
                .productId(UUID.randomUUID())
                .status(true)
                .build();

        ProductResponseDto productResponseDto = ProductResponseDto.builder()
                .productId(product.getProductId())
                .image(product.getUrl())
                .price(product.getPrice())
                .name(product.getName())
                .build();

        List<Product> productList = new ArrayList<>();
        productList.add(product);

        when(productRepository.getAllProducts()).thenReturn(productList);
        when(productMapper.toProductResponseDto(any())).thenReturn(productResponseDto);
        productService.getAllProducts();
        Mockito.verify(redisCacheService).writeListToCachePutAll(any(), any());
    }

    @Test
    void getProductById() {
        Product product = Product.builder()
                .url("https://advivemy-images.s3.us-east-2.amazonaws.com/a3434")
                .price(BigDecimal.valueOf(1012.00))
                .name("aaa")
                .productId(UUID.randomUUID())
                .status(true)
                .build();

        ProductResponseDto productResponseDto = ProductResponseDto.builder()
                .productId(product.getProductId())
                .image(product.getUrl())
                .price(product.getPrice())
                .name(product.getName())
                .build();

        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productMapper.toProductResponseDto(any())).thenReturn(productResponseDto);
        productService.getProductById(any());
        verify(productRepository).findById(any());
    }

    @Test
    void getProductById_ProductNotFoundException() {
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProductById(any()));
        verify(productRepository).findById(any());
    }
}
