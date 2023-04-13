package com.fmss.commondata.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PaymentResponseDto(
        @JsonProperty(namespace = "paymentId")
        UUID id,

        String paymentStatus) {
}
