package com.fmss.basketservice.feign;

import com.fmss.basketservice.model.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/*, configuration = ServiceTwoClientConfig.class)*/
@FeignClient(name = "ProductClient", url = "http://localhost:9003")
public interface ProductClient {

    @GetMapping("/product/{productId}")
    ProductResponseDto getProductById(@PathVariable("productId") UUID productId);

}

