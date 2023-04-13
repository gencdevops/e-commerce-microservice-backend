package com.fmss.orderservice.unit;

import com.fmss.orderservice.model.OrderOutbox;
import com.fmss.orderservice.repository.OrderOutboxRepository;
import com.fmss.orderservice.service.OrderOutBoxService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.Mockito.times;

class OrderOutBoxServiceTest {

    @InjectMocks
    private OrderOutBoxService orderOutboxService;
    @Mock
    private OrderOutboxRepository orderOutboxRepository;

    @Test
    void shouldSaveOutboxRecord() {
        OrderOutbox orderOutbox = new OrderOutbox();
        orderOutboxService.saveOrderOutbox(orderOutbox);
        Mockito.verify(orderOutboxRepository, times(1)).save(orderOutbox);
    }

    @Test
    void shouldDeleteOutboxRecord() {
        UUID orderOutboxId = UUID.randomUUID();
        orderOutboxService.deleteOrderOutbox(orderOutboxId);
        Mockito.verify(orderOutboxRepository, times(1)).deleteById(orderOutboxId);
    }
}
