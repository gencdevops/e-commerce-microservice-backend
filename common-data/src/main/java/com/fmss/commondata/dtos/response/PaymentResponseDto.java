package com.fmss.commondata.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PaymentResponseDto(
        UUID paymentId,

        String paymentStatus) {
}
