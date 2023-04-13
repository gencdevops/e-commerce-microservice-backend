package com.fmss.orderservice.feign;

import com.fmss.commondata.dtos.request.CreatePaymentRequestDto;
import com.fmss.commondata.dtos.response.PaymentResponseDto;
import com.fmss.orderservice.configuration.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "payment-service", configuration = FeignClientConfiguration.class)
public interface PaymentServiceFeignClient {

    @PostMapping(value = "/api/v1/payment")
    PaymentResponseDto createPayment(CreatePaymentRequestDto createPaymentRequestDto);

}
