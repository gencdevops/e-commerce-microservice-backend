package com.fmss.paymentservice.mapper;

import com.fmss.paymentservice.model.dto.CreatePaymentRequestDto;
import com.fmss.paymentservice.model.dto.PaymentResponseDto;
import com.fmss.paymentservice.model.entity.Payment;
import com.fmss.paymentservice.model.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class PaymentMapper {


    public PaymentResponseDto paymentToPaymentRequestDto(Payment payment) {
        return PaymentResponseDto.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .paymentStatus(payment.getPaymentStatus())
                .build();
    }

    public Payment createPaymentRequestDtoToPayment(CreatePaymentRequestDto createPaymentRequestDto) {
        return Payment.builder()
                .id(null)
                .orderId(createPaymentRequestDto.orderId())
                .paymentStatus(PaymentStatus.PENDING)
                .createdDate(ZonedDateTime.now())
                .build();
    }
}
