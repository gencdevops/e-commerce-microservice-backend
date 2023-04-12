package com.fmss.paymentservice.model.dto;

import com.fmss.paymentservice.model.enums.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

@Builder
public record PaymentResponseDto(String paymentId, String orderId, @Enumerated(EnumType.STRING) PaymentStatus paymentStatus) {
}
