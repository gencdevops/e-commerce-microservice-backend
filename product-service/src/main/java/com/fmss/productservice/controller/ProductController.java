package com.fmss.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmss.productservice.model.dto.ProductRequestDto;
import com.fmss.productservice.model.dto.ProductResponseDto;
import com.fmss.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveProduct(@RequestParam("productRequestDto") String productRequestDtoJson, @RequestParam("file") MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ProductRequestDto productRequestDto = objectMapper.readValue(productRequestDtoJson, ProductRequestDto.class);
            productService.createProduct(productRequestDto, file);
            return new ResponseEntity<>("Product saved", HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing productRequestDto JSON", e);
        }
    }
}
