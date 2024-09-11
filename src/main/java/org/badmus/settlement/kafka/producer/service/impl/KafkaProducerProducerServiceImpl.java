package org.badmus.settlement.kafka.producer.service.impl;

import lombok.RequiredArgsConstructor;
import org.badmus.settlement.kafka.producer.service.KafkaProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaProducerProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    @Override
    public void sendMessageToTopic(String topic, String message) {
        CompletableFuture<SendResult<String, Object>> completableFuture = kafkaTemplate.send(topic, 2,
                null, message);
        completableFuture.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" + message + "]due to:[" + ex.getMessage() + "]");
            }
        });
    }
}
