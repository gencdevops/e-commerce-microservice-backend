package com.fmss.productservice.controller;

import com.fmss.productservice.model.dto.ProductResponseDto;
import com.fmss.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.fmss.productservice.constants.ProductConstants.*;

@RestController
@RequestMapping(API_PREFIX + API_VERSION_V1 + API_PRODUCTS)
@RequiredArgsConstructor
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Get all products")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "200",
            description = "All products",
            content = @Content(
                    schema = @Schema(implementation = List.class),
                    mediaType = "application/json")))
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @Operation(summary = "Get product")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "200",
            description = "Get product by productId",
            content = @Content(
                    schema = @Schema(implementation = ProductResponseDto.class),
                    mediaType = "application/json")))
    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductById(@PathVariable UUID productId) {
        return productService.getProductById(productId);
    }

    @Operation(summary = "Create product")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "201",
            description = "Create product",
            content = @Content(
                    schema = @Schema(implementation = String.class),
                    mediaType = "application/json")))
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String saveProduct(@RequestParam("productRequestDto") String productRequestDto, @RequestParam("file") MultipartFile file) {
        return productService.createProduct(productRequestDto, file);
    }
}
