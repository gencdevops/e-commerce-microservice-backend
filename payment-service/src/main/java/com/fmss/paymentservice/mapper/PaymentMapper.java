package com.fmss.paymentservice.mapper;


import com.fmss.commondata.dtos.request.CreatePaymentRequestDto;
import com.fmss.commondata.dtos.response.PaymentResponseDto;
import com.fmss.paymentservice.model.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(implementationName = "PaymentMapperImpl", componentModel = "spring",
        imports = {CreatePaymentRequestDto.class, Payment.class, PaymentResponseDto.class})
public interface PaymentMapper {


    PaymentResponseDto paymentToPaymentResponseDto(Payment payment);

    Payment createPaymentRequestDtoToPayment(CreatePaymentRequestDto createPaymentRequestDto);

}
