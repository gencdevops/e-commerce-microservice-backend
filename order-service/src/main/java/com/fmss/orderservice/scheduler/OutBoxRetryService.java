package com.fmss.orderservice.scheduler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmss.orderservice.model.Order;
import com.fmss.orderservice.repository.OrderOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import service.ProducerService;

@Component
@RequiredArgsConstructor
public class OutBoxRetryService {
    private final OrderOutboxRepository orderOutboxRepository;
    private final ProducerService producerService;
    private final ObjectMapper objectMapper;

    public void retry() {
        orderOutboxRepository.findAll(Pageable.ofSize(10))
                .forEach(orderOutbox -> {
                    producerService.sendMessage(objectMapper.convertValue(orderOutbox.getOrderPayload(), Order.class));
                    orderOutboxRepository.deleteById(orderOutbox.getOrderOutboxId());
                });
    }
}

