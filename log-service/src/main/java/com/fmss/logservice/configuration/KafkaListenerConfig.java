package com.fmss.logservice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@RequiredArgsConstructor
public class    KafkaListenerConfig {
private final ConsumerFactory<String, Object> consumerFactory;
private final KafkaTemplate<String, Object> kafkaTemplate;

@Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaBlockingRetryContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, String> factory=new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(new DefaultErrorHandler(
        new DeadLetterPublishingRecoverer(kafkaTemplate),new FixedBackOff(5000,3))
        );
        return factory;
        }
        }