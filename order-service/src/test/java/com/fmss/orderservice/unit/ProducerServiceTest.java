package com.fmss.orderservice.unit;

import com.fmss.orderservice.model.Order;
import com.fmss.orderservice.service.ProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {
    @InjectMocks
    private ProducerService producerService;
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void shouldSendMessage() {
        String topic = "order_placed";
        ReflectionTestUtils.setField(producerService, "producerTopic", topic);

        Order order = new Order();
        ArgumentCaptor<Object> messageCaptor = ArgumentCaptor.forClass(Object.class);

        producerService.sendMessage(order);

        Mockito.verify(kafkaTemplate).send(eq(topic), messageCaptor.capture());
        assertEquals(order, messageCaptor.getValue());
    }
}
