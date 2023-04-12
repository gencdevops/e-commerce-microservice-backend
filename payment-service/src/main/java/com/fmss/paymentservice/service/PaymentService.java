package com.fmss.paymentservice.service;

import com.fmss.commondata.dtos.request.CreatePaymentRequestDto;
import com.fmss.commondata.dtos.response.PaymentResponseDto;
import com.fmss.paymentservice.exceptions.PaymentNotFoundException;
import com.fmss.paymentservice.mapper.PaymentMapper;
import com.fmss.paymentservice.model.entity.Payment;
import com.fmss.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {


    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentResponseDto createPayment(CreatePaymentRequestDto createPaymentRequestDto) {
        Payment payment = paymentMapper.convertPaymentFromCreatePaymentRequestDto(createPaymentRequestDto);
        paymentRepository.save(payment);
        return paymentMapper.convertPaymentResponseDtoFromPayment(payment);
    }

    public PaymentResponseDto getPaymentByOrderId(String orderId) {
        return paymentRepository.findPaymentByOrderId(orderId)
                        .map(paymentMapper::convertPaymentResponseDtoFromPayment)
                        .orElseThrow(()-> new PaymentNotFoundException("Payment Not Found!"));
    }

    public PaymentStatus getPaymentStatus(String paymentId) {
        return paymentRepository.findById(paymentId)
                .map(Payment::getPaymentStatus)
                .orElseThrow(()-> new PaymentNotFoundException("Payment Not Found!"));
    }

}
