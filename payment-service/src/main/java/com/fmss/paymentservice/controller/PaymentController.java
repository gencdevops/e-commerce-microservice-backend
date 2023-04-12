package com.fmss.paymentservice.controller;

import com.fmss.paymentservice.model.dto.CreatePaymentRequestDto;
import com.fmss.paymentservice.model.dto.PaymentResponseDto;
import com.fmss.paymentservice.model.entity.Payment;
import com.fmss.paymentservice.model.enums.PaymentStatus;
import com.fmss.paymentservice.repository.PaymentRepository;
import com.fmss.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    public final PaymentService paymentService;
    public final PaymentRepository paymentRepository;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(CreatePaymentRequestDto createPaymentRequestDto) {
        return ResponseEntity.ok(paymentService.createPayment(createPaymentRequestDto));
    }

    @GetMapping
    public ResponseEntity<PaymentResponseDto> getPaymentByOrderId(String orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }
}
