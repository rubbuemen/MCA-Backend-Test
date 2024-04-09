package com.mca.infrastructure.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class KafkaMessageProducer<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;

    @Value(value = "${topic}")
    private String topicName;

    public KafkaMessageProducer(KafkaTemplate<String, T> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(T message) {
        this.kafkaTemplate.send(topicName, message);
    }
}
