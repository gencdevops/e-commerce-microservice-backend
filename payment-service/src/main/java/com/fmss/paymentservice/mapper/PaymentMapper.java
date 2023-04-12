package com.fmss.paymentservice.mapper;

import com.fmss.paymentservice.model.dto.CreatePaymentRequestDto;
import com.fmss.paymentservice.model.dto.PaymentResponseDto;
import com.fmss.paymentservice.model.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(implementationName = "PaymentMapperImpl", componentModel = "spring")
public interface PaymentMapper {


    @Mapping(target = "paymentStatus", expression = "java(payment.getPaymentStatus().toString())")
    PaymentResponseDto convertPaymentResponseDtoFromPayment(Payment payment);


    Payment convertPaymentFromCreatePaymentRequestDto(CreatePaymentRequestDto createPaymentRequestDto);

}
