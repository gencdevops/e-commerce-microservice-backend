package com.fmss.commondata.dtos.response;

import com.fmss.commondata.model.enums.PaymentStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PaymentResponseDto(
        UUID paymentId,

        PaymentStatus paymentStatus) {
}
