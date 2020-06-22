package com.trainingtask.dataprocessor.config;

import com.trainingtask.dataprocessor.entity.DailyStatistic;
import com.trainingtask.dataprocessor.entity.GeneralStatistic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka.group-id}")
    private String groupId;
    @Value("${kafka.bootstrap-address}")
    private String bootstrapAddress;

    public ConsumerFactory<String, GeneralStatistic> generalStatisticConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(GeneralStatistic.class));
    }

    public ConsumerFactory<String, DailyStatistic> dailyStatisticConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(DailyStatistic.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GeneralStatistic> generalStatisticKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, GeneralStatistic> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(generalStatisticConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DailyStatistic> dailyStatisticKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, DailyStatistic> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(dailyStatisticConsumerFactory());
        return factory;
    }
}