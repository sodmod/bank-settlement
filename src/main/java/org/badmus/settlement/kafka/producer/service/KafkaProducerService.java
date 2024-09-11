package org.badmus.settlement.kafka.producer.service;

public interface KafkaProducerService {
    void sendMessageToTopic(String topic, String message);
}
