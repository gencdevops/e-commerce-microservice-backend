package com.fmss.paymentservice.service;

import com.fmss.paymentservice.model.entity.Payment;
import com.fmss.paymentservice.model.enums.PaymentStatus;
import com.fmss.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {


    private final PaymentRepository paymentRepository;

    public Payment createPayment(String orderId, PaymentStatus paymentStatus) {
        return paymentRepository.save(new Payment(orderId, paymentStatus));
    }
}
