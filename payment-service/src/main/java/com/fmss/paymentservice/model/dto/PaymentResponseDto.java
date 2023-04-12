package com.fmss.paymentservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record PaymentResponseDto(
        @JsonProperty(namespace = "paymentId")
        String id,

        String paymentStatus) {
}
