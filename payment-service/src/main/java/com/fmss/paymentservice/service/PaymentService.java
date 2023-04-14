package com.fmss.paymentservice.service;

import com.fmss.commondata.configuration.UserContext;
import com.fmss.commondata.dtos.request.CreatePaymentRequestDto;
import com.fmss.commondata.dtos.response.PaymentResponseDto;
import com.fmss.commondata.model.enums.PaymentStatus;
import com.fmss.paymentservice.mapper.PaymentMapper;
import com.fmss.paymentservice.model.entity.Payment;
import com.fmss.paymentservice.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public PaymentResponseDto createPayment(CreatePaymentRequestDto createPaymentRequestDto) {
        Payment payment = paymentMapper.createPaymentRequestDtoToPayment(createPaymentRequestDto);
        payment.setPaymentStatus(PaymentStatus.APPROVAL);
        Payment paymentCreated = paymentRepository.saveAndFlush(payment);
        log.info("Created payment {}", paymentCreated.getPaymentId());
        return paymentMapper.paymentToPaymentResponseDto(payment);
    }
}
