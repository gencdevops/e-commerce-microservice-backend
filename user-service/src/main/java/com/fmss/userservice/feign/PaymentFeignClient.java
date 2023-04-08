package com.fmss.userservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "payment-service")
public interface PaymentFeignClient {

    @GetMapping("/api/v1/test")
    String testFeign();
}
