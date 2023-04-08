package com.fmss.userservice.controller;

import com.fmss.userservice.feign.BasketFeignClient;
import com.fmss.userservice.feign.OrderFeignClient;
import com.fmss.userservice.feign.PaymentFeignClient;
import com.fmss.userservice.feign.ProductFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class FeignTestController {

    private final BasketFeignClient basketFeignClient;

    private final OrderFeignClient orderFeignClient;

    private final PaymentFeignClient paymentFeignClient;

    private final ProductFeignClient productFeignClient;

    @GetMapping
    public String testFeign(){
        String text = "";
            text += basketFeignClient.testFeign() + "\n";
            text += orderFeignClient.testFeign() + "\n";
            text += paymentFeignClient.testFeign() + "\n";
            text += productFeignClient.testFeign();
        return text;
    }
}
