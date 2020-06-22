package com.trainingtask.dataprocessor.test;

import com.trainingtask.dataprocessor.entity.DailyStatistic;
import com.trainingtask.dataprocessor.entity.GeneralStatistic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kafka/publish")
public class KafkaController {

    private final KafkaTemplate<String, GeneralStatistic> generalStatisticKafkaTemplate;
    private final KafkaTemplate<String, DailyStatistic> dailyStatisticKafkaTemplate;

    public KafkaController(
            KafkaTemplate<String, GeneralStatistic> generalStatisticKafkaTemplate,
            KafkaTemplate<String, DailyStatistic> dailyStatisticKafkaTemplate) {

        this.generalStatisticKafkaTemplate = generalStatisticKafkaTemplate;
        this.dailyStatisticKafkaTemplate = dailyStatisticKafkaTemplate;
    }

    @PostMapping(value = "/aggregated-statistic")
    public void sendGeneralStatisticMessageToKafkaTopic(
            @RequestBody GeneralStatistic message,
            @Value("${kafka.topics.aggregated-statistic.name}") String topic) {

        this.generalStatisticKafkaTemplate.send(topic, message);
    }

    @PostMapping(value = "/daily-statistic")
    public void sendDailyStatisticMessageToKafkaTopic(
            @RequestBody DailyStatistic message,
            @Value("${kafka.topics.daily-statistic.name}") String topic) {

        this.dailyStatisticKafkaTemplate.send(topic, message);
    }
}