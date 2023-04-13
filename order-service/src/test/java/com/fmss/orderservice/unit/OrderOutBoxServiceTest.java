package com.fmss.orderservice.unit;

import com.fmss.orderservice.model.OrderOutbox;
import com.fmss.orderservice.repository.OrderOutboxRepository;
import com.fmss.orderservice.service.OrderOutBoxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderOutBoxServiceTest {

    @InjectMocks
    private OrderOutBoxService orderOutboxService;
    @Mock
    private OrderOutboxRepository orderOutboxRepository;

    @Test
    void shouldSaveOutboxRecord() {
        OrderOutbox orderOutbox = OrderOutbox.builder()
                .orderOutboxId(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .build();
        orderOutboxService.saveOrderOutbox(orderOutbox);
        Mockito.verify(orderOutboxRepository, times(1)).save(orderOutbox);
    }

    @Test
    void shouldDeleteOutboxRecord() {
        UUID orderOutboxId = UUID.randomUUID();
        orderOutboxService.deleteOrderOutbox(orderOutboxId);
        Mockito.verify(orderOutboxRepository, times(1)).deleteByOrderId(orderOutboxId);
    }
}
