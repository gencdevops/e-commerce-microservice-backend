package com.fmss.paymentservice.mapper;


import com.fmss.commondata.dtos.request.CreatePaymentRequestDto;
import com.fmss.commondata.dtos.response.PaymentResponseDto;
import com.fmss.paymentservice.model.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(implementationName = "PaymentMapperImpl", componentModel = "spring")
public interface PaymentMapper {


    @Mapping(target = "paymentStatus", expression = "java(payment.getPaymentStatus().toString())")
    PaymentResponseDto convertPaymentResponseDtoFromPayment(Payment payment);


    Payment convertPaymentFromCreatePaymentRequestDto(CreatePaymentRequestDto createPaymentRequestDto);

}
