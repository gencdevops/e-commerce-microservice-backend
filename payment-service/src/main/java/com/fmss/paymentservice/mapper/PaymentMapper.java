package com.fmss.paymentservice.mapper;

import com.fmss.paymentservice.model.dto.CreatePaymentRequestDto;
import com.fmss.paymentservice.model.dto.PaymentResponseDto;
import com.fmss.paymentservice.model.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(implementationName = "PaymentMapperImpl", componentModel = "spring")
public interface PaymentMapper {


    @Mapping(target = "paymentStatus", expression = "java(payment.getPaymentStatus().toString())")
    PaymentResponseDto convertPaymentResponseDtoFromPayment(Payment payment);

    Payment convertPaymentFromCreatePaymentRequestDto(CreatePaymentRequestDto createPaymentRequestDto);

//    public PaymentResponseDto paymentToPaymentRequestDto(Payment payment) {
//        return PaymentResponseDto.builder()
//                .paymentId(payment.getId())
//                .orderId(payment.getOrderId())
//                .userId(payment.getUserId())
//                .paymentStatus(payment.getPaymentStatus())
//                .build();
//    }

//    public Payment createPaymentRequestDtoToPayment(CreatePaymentRequestDto createPaymentRequestDto) {
//        return Payment.builder()
//                .id(null)
//                .orderId(createPaymentRequestDto.orderId())
//                .userId(createPaymentRequestDto.userId())
//                .paymentStatus(PaymentStatus.PENDING)
//                .createdDate(ZonedDateTime.now())
//                .createdBy(createPaymentRequestDto.userId())
//                .build();
//    }
}
