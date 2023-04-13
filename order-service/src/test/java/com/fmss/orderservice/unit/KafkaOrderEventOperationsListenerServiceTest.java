package com.fmss.orderservice.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmss.commondata.model.enums.OrderStatus;
import com.fmss.orderservice.model.Order;
import com.fmss.orderservice.repository.OrderRepository;
import com.fmss.orderservice.service.KafkaOrderEventOperationsListenerService;
import com.fmss.orderservice.service.OrderOutBoxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaOrderEventOperationsListenerServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    ObjectMapper objectMapper;

    @Mock
    OrderOutBoxService orderOutBoxService;
    @InjectMocks
    KafkaOrderEventOperationsListenerService kafkaOrderEventOperationsListenerService;

    @Test
    void shouldSetStatusAndSaveOrder() throws JsonProcessingException {
        UUID uuid = UUID.randomUUID();

        Order mockOrder = Mockito.spy(Order.class);
        mockOrder.setOrderId(uuid);

        when(objectMapper.readValue(any(String.class), any(Class.class))).thenReturn(mockOrder);
        when(orderRepository.findById(uuid)).thenReturn(Optional.of(mockOrder));
        when(orderRepository.saveAndFlush(mockOrder)).thenReturn(mockOrder);

        kafkaOrderEventOperationsListenerService.handleMessage("test", "test");

        verify(mockOrder).setOrderStatus(OrderStatus.PREPARING);
        verify(orderRepository).saveAndFlush(mockOrder);
        verify(orderOutBoxService).deleteOrderOutbox(any());
    }
}