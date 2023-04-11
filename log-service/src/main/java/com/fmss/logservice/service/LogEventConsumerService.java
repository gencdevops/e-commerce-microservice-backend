package com.fmss.logservice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fmss.logservice.model.LogModel;
import com.fmss.logservice.repository.LogModelRepository;
import com.fmss.logservice.utils.FileWriterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogEventConsumerService {

    private final LogModelRepository logModelRepository;

    @RetryableTopic(
            attempts = "5",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            exclude = {SerializationException.class, DeserializationException.class, JsonProcessingException.class}
    )
    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.group.id}")
    public void handleMessage(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws JsonProcessingException {
        LogModel logModel1 = new LogModel(null, message);
        log.info(message);
        FileWriterUtil.writeToFile(logModel1);
        logModelRepository.save(logModel1);
        System.out.println(logModelRepository.findAll().size());
    }
}






