package com.fmss.paymentservice.controller;


import com.fmss.commondata.dtos.request.CreatePaymentRequestDto;
import com.fmss.commondata.dtos.response.PaymentResponseDto;
import com.fmss.commondata.model.enums.PaymentStatus;
import com.fmss.paymentservice.repository.PaymentRepository;
import com.fmss.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    public final PaymentService paymentService;
    public final PaymentRepository paymentRepository;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(CreatePaymentRequestDto createPaymentRequestDto) {
        return ResponseEntity.status(201).body(paymentService.createPayment(createPaymentRequestDto));
    }
}
