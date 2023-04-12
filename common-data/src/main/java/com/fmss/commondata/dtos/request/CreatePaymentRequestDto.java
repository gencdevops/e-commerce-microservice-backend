package com.fmss.commondata.dtos.request;


import lombok.Builder;

@Builder
public record CreatePaymentRequestDto(String orderId, String userId) {
}
