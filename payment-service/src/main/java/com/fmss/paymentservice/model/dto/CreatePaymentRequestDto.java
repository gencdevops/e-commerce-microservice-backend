package com.fmss.paymentservice.model.dto;


import lombok.Builder;

@Builder
public record CreatePaymentRequestDto(String orderId, String userId) {
}
