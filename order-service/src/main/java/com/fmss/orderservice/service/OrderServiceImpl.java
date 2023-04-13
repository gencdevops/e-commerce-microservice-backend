package com.fmss.orderservice.service;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.fmss.commondata.dtos.request.CreatePaymentRequestDto;
import com.fmss.commondata.dtos.response.OrderResponseDTO;
import com.fmss.commondata.dtos.response.PaymentResponseDto;
import com.fmss.commondata.model.enums.OrderStatus;
import com.fmss.commondata.model.enums.PaymentStatus;

import com.fmss.orderservice.dto.PlaceOrderRequestDTO;
import com.fmss.orderservice.exception.PaymentFailureException;
import com.fmss.orderservice.feign.PaymentServiceFeignClient;
import com.fmss.orderservice.mapper.OrderMapper;
import com.fmss.orderservice.model.Order;
import com.fmss.orderservice.model.OrderOutbox;

import com.fmss.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ObjectMapper objectMapper;
    private final ProducerService producerService;
    private final OrderOutBoxService orderOutBoxService;
    private final PaymentServiceFeignClient paymentServiceFeignClient;


    @Override
    @Transactional
    public OrderResponseDTO placeOrder(@NotNull PlaceOrderRequestDTO placeOrderRequestDTO) {
        var order = orderMapper.convertOrderFromPlaceOrderRequestDTO(placeOrderRequestDTO);
        order.setOrderStatus(OrderStatus.RECEIVED);
        Order orderCreated = orderRepository.saveAndFlush(order);
        log.info("Created order {}", order.getOrderId());


        CreatePaymentRequestDto createPaymentRequestDto = new CreatePaymentRequestDto(orderCreated.getOrderId()
                , placeOrderRequestDTO.userId());
        PaymentResponseDto paymentResponse = paymentServiceFeignClient.createPayment(createPaymentRequestDto);

        if (Objects.isNull(paymentResponse) || !paymentResponse.paymentStatus().equals(PaymentStatus.APPROVAL.toString())) {
            String errorLogMessage = "Payment failure";
            log.error("Payment failure {}, message : {}", paymentResponse.paymentId(), errorLogMessage);
            throw new PaymentFailureException(errorLogMessage);
        }
        producerService.sendMessage(orderCreated);


        OrderOutbox orderOutbox;
        try {
            orderOutbox = OrderOutbox.builder()
                    .orderPayload(objectMapper.writeValueAsString(order))
                    .paymentId(String.valueOf(paymentResponse.paymentId()))
                    .orderId(orderCreated.getOrderId())
                    .build();
            OrderOutbox savedOrderOutbox = orderOutBoxService.saveOrderOutbox(orderOutbox);
            log.info("order outbox saved database : {}", savedOrderOutbox.getOrderOutboxId());
        } catch (Exception e) {
            log.error("Error order outbox {}", e.getMessage());
        }


        return orderMapper.convertOrderFResponseDtoFromOrder(orderCreated);
    }


}






