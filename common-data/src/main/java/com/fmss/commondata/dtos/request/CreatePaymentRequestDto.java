package com.fmss.commondata.dtos.request;


import lombok.Builder;

import java.util.UUID;

@Builder
public record CreatePaymentRequestDto(
        UUID orderId,

        UUID userId) {
}
