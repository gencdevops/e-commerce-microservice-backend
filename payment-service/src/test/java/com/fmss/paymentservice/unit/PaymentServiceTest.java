package com.fmss.paymentservice.unit;

import com.fmss.commondata.dtos.request.CreatePaymentRequestDto;
import com.fmss.commondata.dtos.response.PaymentResponseDto;
import com.fmss.commondata.model.enums.PaymentStatus;
import com.fmss.paymentservice.mapper.PaymentMapper;
import com.fmss.paymentservice.model.entity.Payment;
import com.fmss.paymentservice.repository.PaymentRepository;
import com.fmss.paymentservice.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;

    @Mock
    PaymentMapper paymentMapper;

    @Mock
    PaymentRepository paymentRepository;

    @Test
    void createPayment() {
        CreatePaymentRequestDto createPaymentRequestDto = CreatePaymentRequestDto.builder()
                .userId(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .build();

        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID())
                .orderId(createPaymentRequestDto.orderId())
                .userId(createPaymentRequestDto.userId())
                .build();

        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .paymentId(payment.getPaymentId())
                .build();

        when(paymentMapper.createPaymentRequestDtoToPayment(any())).thenReturn(payment);
        when(paymentRepository.saveAndFlush(any())).thenReturn(payment);
        when(paymentMapper.paymentToPaymentResponseDto(any())).thenReturn(paymentResponseDto);
        paymentService.createPayment(createPaymentRequestDto);
        verify(paymentRepository, times(1)).saveAndFlush(any());
    }

}
