package com.fmss.orderservice.unit;

import com.fmss.commondata.dtos.response.BasketItemResponseDto;
import com.fmss.commondata.dtos.response.BasketResponseDto;
import com.fmss.commondata.dtos.response.PaymentResponseDto;
import com.fmss.commondata.model.enums.OrderStatus;
import com.fmss.commondata.model.enums.PaymentStatus;
import com.fmss.orderservice.dto.PlaceOrderRequestDTO;
import com.fmss.orderservice.exception.PaymentFailureException;
import com.fmss.orderservice.feign.PaymentServiceFeignClient;
import com.fmss.orderservice.mapper.OrderMapper;
import com.fmss.orderservice.model.Order;
import com.fmss.orderservice.repository.OrderRepository;
import com.fmss.orderservice.service.OrderOutBoxService;
import com.fmss.orderservice.service.OrderServiceImpl;
import com.fmss.orderservice.service.ProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderMapper orderMapper;

    @Mock
    ProducerService producerService;

    @Mock
    OrderOutBoxService orderOutBoxService;

    @Mock
    PaymentServiceFeignClient paymentServiceFeignClient;

    @Mock
    OrderServiceImpl orderService;

    @Test
    void placeOrder_PaymentFailure_PaymentFailureException() {
        BasketItemResponseDto basketItemResponseDto = BasketItemResponseDto.builder()
                .basketItemId(UUID.randomUUID())
                .name("Süt")
                .imgUrl("https://advivemy-images.s3.us-east-2.amazonaws.com/sut")
                .productId(UUID.randomUUID())
                .quantity(21)
                .build();

        BasketResponseDto basketResponseDto = BasketResponseDto.builder()
                .basketId(UUID.randomUUID())
                .basketItemList(List.of(basketItemResponseDto))
                .totalPrice(BigDecimal.valueOf(1000))
                .build();

        PlaceOrderRequestDTO placeOrderRequestDTO = PlaceOrderRequestDTO.builder()
                .userId(UUID.randomUUID())
                .basketResponseDto(basketResponseDto)
                .build();

        Order order = Order.builder()
                .totalPrice(basketResponseDto.totalPrice())
                .build();

        Order orderCreated = Order.builder()
                .orderId(UUID.randomUUID())
                .orderStatus(OrderStatus.RECEIVED)
                .totalPrice(order.getTotalPrice())
                .build();

        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .paymentId(UUID.randomUUID())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        when(orderMapper.convertOrderFromPlaceOrderRequestDTO(placeOrderRequestDTO)).thenReturn(order);
        when(orderRepository.saveAndFlush(any())).thenReturn(orderCreated);
        when(paymentServiceFeignClient.createPayment(any())).thenReturn(null);

        assertThrows(PaymentFailureException.class, () -> orderService.placeOrder(placeOrderRequestDTO));
    }



}