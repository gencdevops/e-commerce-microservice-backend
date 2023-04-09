package com.fmss.commondata.kafka;


import com.fmss.commondata.model.LogModelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaLogSenderService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.log.topic}")
    private  String producerTopic;



    public void sendLog(String logMessage) {
        try {
            log.info("Order sendMessage()   {}", logMessage);
            kafkaTemplate.send(producerTopic,  logMessage);
        }catch (Exception e){

        }
    }

}
