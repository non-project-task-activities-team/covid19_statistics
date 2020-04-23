package com.trainingtask.dataprocessor.listener;

import com.trainingtask.dataprocessor.model.DailyStatistic;
import com.trainingtask.dataprocessor.repository.MongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    @Autowired
    private MongoRepository mongoRepository;

    @KafkaListener(
            topics = "${kafka.statistic.topic.name}",
            groupId = "statistic",
            containerFactory = "statisticKafkaListenerContainerFactory")
    public void consume(DailyStatistic message) {
        log.info("message consumed:" + message);
        mongoRepository.upsert(message);
    }

}
