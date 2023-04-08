package com.fmss.userservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "basket-service")
public interface BasketFeignClient {

    @GetMapping("/api/v1/test")
    String testFeign();

}
