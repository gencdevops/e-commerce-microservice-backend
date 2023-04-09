package com.fmss.userservice.controller;

import com.fmss.userservice.feign.BasketFeignClient;
import com.fmss.userservice.feign.OrderFeignClient;
import com.fmss.userservice.feign.PaymentFeignClient;
import com.fmss.userservice.feign.ProductFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;


@RestController
@RequestMapping("/test")
@Slf4j
@RequiredArgsConstructor
public class FeignTestController {

    private final BasketFeignClient basketFeignClient;

    private final OrderFeignClient orderFeignClient;

    private final PaymentFeignClient paymentFeignClient;

    private final ProductFeignClient productFeignClient;

    @GetMapping
    public String testFeign(){
        log.info("Hello from payment service.");
        String text = "";
        try {
            text += basketFeignClient.testFeign() + "\n";
        } catch (Exception exception) {log.error("Basket e bağlanamadım");}
        try {
            text += orderFeignClient.testFeign() + "\n";
        } catch (Exception exception) {log.error("Order a bağlanamadım");}
        try {
            text += paymentFeignClient.testFeign() + "\n";
        } catch (Exception exception) {log.error("Payment e bağlanamadım");}
        try {
            text += productFeignClient.testFeign();
        } catch (Exception exception) {log.error("Product a bağlanamadım");}
        return text;
    }
}
