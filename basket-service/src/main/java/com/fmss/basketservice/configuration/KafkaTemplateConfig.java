package com.fmss.basketservice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@RequiredArgsConstructor
public class KafkaTemplateConfig {

    private final ProducerFactory<String, Object> producerFactory;

    @Bean
    public KafkaTemplate<String, Object> getKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory);
    }


}
