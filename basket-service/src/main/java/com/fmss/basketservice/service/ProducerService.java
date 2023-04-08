package com.fmss.basketservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService implements ApplicationRunner {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.producer.topic}")
    private  String producerTopic;



    public void sendMessage(String logMessage) {
        try {
            log.info("log :    {}", logMessage);
            kafkaTemplate.send(producerTopic,  logMessage);
        }catch (Exception e){

        }
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        sendMessage("basket service log");
    }
}
