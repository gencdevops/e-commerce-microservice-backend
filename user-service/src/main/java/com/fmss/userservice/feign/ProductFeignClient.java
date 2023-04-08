package com.fmss.userservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;


@FeignClient(name = "product-service")
public interface ProductFeignClient {

    @GetMapping("/api/v1/test")
    String testFeign();
}
