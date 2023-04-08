package service;


import com.fmss.orderservice.model.OrderOutbox;
import com.fmss.orderservice.repository.OrderOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderOutBoxService {
    private final OrderOutboxRepository orderOutboxRepository;

    public void saveOrderOutbox(OrderOutbox orderOutbox) {
        orderOutboxRepository.save(orderOutbox);

    }

    public void deleteOrderOutbox(UUID orderId) {
        orderOutboxRepository.deleteByOrderId(orderId);
    }
}

