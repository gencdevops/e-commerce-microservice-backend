package com.fmss.orderservice.service;



import com.fmss.commondata.dtos.response.OrderResponseDTO;
import com.fmss.orderservice.dto.PlaceOrderRequestDTO;
import jakarta.validation.constraints.NotNull;


public interface OrderService {
    OrderResponseDTO placeOrder(@NotNull PlaceOrderRequestDTO placeOrderRequestDTO);

}
