package com.fmss.orderservice.unit;

import com.fmss.orderservice.model.OrderOutbox;
import com.fmss.orderservice.repository.OrderOutboxRepository;
import com.fmss.orderservice.scheduler.OutBoxRetryService;
import com.fmss.orderservice.service.ProducerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OutBoxRetryServiceTest {

    @Mock
    OrderOutboxRepository orderOutboxRepository;

    @Mock
    ProducerService producerService;

    @InjectMocks
    OutBoxRetryService outBoxRetryService;

    @Test
    void retry() {
        Page<OrderOutbox> page = Page.empty();
        when(orderOutboxRepository.findAll(Pageable.ofSize(10))).thenReturn(page);

        outBoxRetryService.retry();

        verify(producerService, times(0)).sendMessage(any());
        verify(orderOutboxRepository, times(0)).deleteById(any());
    }
}
