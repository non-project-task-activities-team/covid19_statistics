package com.trainingtask.dataprocessor.listener;

import com.trainingtask.dataprocessor.entity.DailyStatistic;
import com.trainingtask.dataprocessor.entity.GeneralStatistic;
import com.trainingtask.dataprocessor.repository.DailyStatisticMongoRepository;
import com.trainingtask.dataprocessor.repository.GeneralStatisticMongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    @Autowired
    private DailyStatisticMongoRepository dailyStatisticMongoRepository;
    @Autowired
    private GeneralStatisticMongoRepository generalStatisticMongoRepository;

    @KafkaListener(
            topics = "${kafka.topics.general-statistic.name}",
            groupId = "statistic",
            containerFactory = "generalStatisticKafkaListenerContainerFactory")
    public void consumeGeneralStatisticMessage(GeneralStatistic message) {
        log.info("message consumed:" + message);
        generalStatisticMongoRepository.upsert(message);
    }

    @KafkaListener(
            topics = "${kafka.topics.daily-statistic.name}",
            groupId = "statistic",
            containerFactory = "dailyStatisticKafkaListenerContainerFactory")
    public void consumeDailyStatisticMessage(DailyStatistic message) {
        log.info("message consumed:" + message);
        dailyStatisticMongoRepository.upsert(message);
    }
}
