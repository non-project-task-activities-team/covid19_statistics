package com.trainingtask.dataprocessor.test;

import com.trainingtask.dataprocessor.model.DailyStatistic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducer {

    private static final String TOPIC = "statistic";

    @Autowired
    private KafkaTemplate<String, DailyStatistic> kafkaTemplate;

    public void sendMessage(DailyStatistic message) {
        log.info("Produced message: " + message);
        this.kafkaTemplate.send(TOPIC, message);
    }
}