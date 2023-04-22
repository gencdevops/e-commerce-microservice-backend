package com.fmss.orderservice.service;


import com.fmss.orderservice.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final SlackReportingService slackReportingService;

    @Value("${spring.kafka.producer.topic}")
    private  String producerTopic;

    public void sendMessage(Order order) {
        try {
            log.info("Order send kafka topic :    {}", order);
            kafkaTemplate.send(producerTopic,  order);
        }catch (Exception e){
            slackReportingService.sendErrorMessage("Order Outbox Retry Error", e);
        }
    }
}

