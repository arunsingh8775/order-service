package com.example.orderservice.config;

import com.example.ordertrackingcommon.model.OrderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    private final ProducerFactory<String, OrderEvent> producerFactory;

    public KafkaConfig(ProducerFactory<String, OrderEvent> producerFactory) {
        this.producerFactory = producerFactory;
    }

    @Bean
    public KafkaTemplate<String, OrderEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory);
    }
}
