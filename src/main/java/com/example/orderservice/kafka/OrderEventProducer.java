package com.example.orderservice.kafka;

import com.example.ordertrackingcommon.model.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    public static final String ORDER_CREATED_TOPIC = "orders_topic";

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderEvent(OrderEvent event) {
        kafkaTemplate.send(ORDER_CREATED_TOPIC, event);
        System.out.println("Published event to Kafka: " + event);
    }
}