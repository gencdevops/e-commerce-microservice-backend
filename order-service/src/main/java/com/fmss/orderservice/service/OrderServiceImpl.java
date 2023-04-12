package service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fmss.orderservice.dto.OrderResponseDTO;
import com.fmss.orderservice.dto.PlaceOrderRequestDTO;
import com.fmss.orderservice.mapper.OrderMapper;
import com.fmss.orderservice.model.Order;
import com.fmss.orderservice.model.enums.OrderStatus;
import com.fmss.orderservice.repository.OrderRepository;
import com.fmss.orderservice.service.OrderOutBoxService;
import com.fmss.orderservice.service.OrderService;
import com.fmss.orderservice.service.ProducerService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;



    private final ObjectMapper objectMapper;
    private final ProducerService producerService;
    private final OrderOutBoxService orderOutBoxService;


    @Override
    @Transactional
    public OrderResponseDTO placeOrder(@NotNull PlaceOrderRequestDTO placeOrderRequestDTO) {


        var order = orderMapper.convertOrderFromPlaceOrderRequestDTO(placeOrderRequestDTO);

        order.setOrderStatus(OrderStatus.RECEIVED);

        Order orderCreated = orderRepository.saveAndFlush(order);


        PaymentResponse paymentResponse = paymentSystem.pay(placeOrderRequestDTO.getOrderItems(),
                cardMapper.convertCardFromCardInfoDto(placeOrderRequestDTO.getCardInfo()));


        if (Objects.isNull(paymentResponse) || !paymentResponse.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
            throw new PaymentFailureException("Payment failure");
        }
        producerService.sendMessage(orderCreated);

        OrderOutbox orderOutbox;
        try {
            orderOutbox = OrderOutbox.builder()
                    .orderPayload(objectMapper.writeValueAsString(order))
                    .paymentId(String.valueOf(paymentResponse.getId()))
                    .orderId(orderCreated.getOrderId())
                    .build();
            orderOutBoxService.saveOrderOutbox(orderOutbox);
        } catch (Exception e) {
            // TODO:Buradaki orderoutbox kayitlari baska bir db;ye atilabilir diye dusundum
            log.error("Error order outbox {}", e.getMessage());
        }


        return orderMapper.convertPlaceOrderResponseDTOFromOrder(orderCreated, orderItems);
    }




    }





    }
