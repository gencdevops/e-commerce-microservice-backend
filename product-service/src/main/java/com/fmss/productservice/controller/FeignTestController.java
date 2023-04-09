package com.fmss.productservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class FeignTestController {


    @GetMapping("/test")
    public String testFeign(){
        log.info("Hello from payment service.");
        return "Hello from product service.";
    }
}