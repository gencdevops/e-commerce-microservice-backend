package com.fmss.commondata.dtos.response;


import com.fmss.commondata.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderResponseDTO {
    private UUID orderId;
    private BigDecimal totalPrice;

    private OrderStatus orderStatus;
}
