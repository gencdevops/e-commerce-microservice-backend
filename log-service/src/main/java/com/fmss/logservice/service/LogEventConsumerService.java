package com.fmss.logservice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmss.logservice.model.LogModel;
import com.fmss.logservice.repository.LogModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.kafka.support.serializer.DeserializationException;


import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogEventConsumerService {

    private final ObjectMapper objectMapper;
    private final LogModelRepository logModelRepository;


    @RetryableTopic(
            attempts = "5",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            exclude = {SerializationException.class, DeserializationException.class, JsonProcessingException.class}
    )
    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.group.id}")
    public void handleMessage(String logModel, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws JsonProcessingException {
        LogModel logModelString = objectMapper.readValue(logModel, LogModel.class);
        log.info("Order -> {}", logModel);
        Optional<LogModel> logModel1Optional = logModelRepository.findById(logModelString.toString());
        logModel1Optional.ifPresent(logModelRepository::save);

    }

    @DltHandler
    public void handleDlt(String logModel, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Message: {} handled by dlq topic: {}", logModel, topic);
        // TODO : Dead Letter queue'ya gelen Mongo'ya falan basilabilir
    }



}






