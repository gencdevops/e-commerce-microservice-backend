package com.fmss.commondata.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record PaymentResponseDto(
        @JsonProperty(namespace = "paymentId")
        String id,

        String paymentStatus) {
}
