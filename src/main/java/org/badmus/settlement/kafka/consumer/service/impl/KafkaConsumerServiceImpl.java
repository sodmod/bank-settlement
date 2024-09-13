package org.badmus.settlement.kafka.consumer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badmus.settlement.cloudinary.CloudinaryService;
import org.badmus.settlement.kafka.consumer.service.KafkaConsumerService;
import org.badmus.settlement.settle.service.SettlementService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private final SettlementService settlementService;

    @Override
    @KafkaListener(topics = {"settlement-consumer"}, groupId = "settlement",
            topicPartitions = {@TopicPartition(topic = "topic-name", partitions = {"1", "2"})})
    public void consumeSettlement(String settlement){

        settlementService.consumeSettlementFromKafkaString(settlement);

        log.info("Consumed message: {}", settlement);
    }

}
