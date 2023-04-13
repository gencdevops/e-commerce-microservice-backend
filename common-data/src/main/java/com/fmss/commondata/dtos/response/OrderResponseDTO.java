package com.fmss.commondata.dtos.response;


import com.fmss.commondata.model.enums.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderResponseDTO (
        UUID orderId,

        BigDecimal totalPrice,

        OrderStatus orderStatus) {
}
