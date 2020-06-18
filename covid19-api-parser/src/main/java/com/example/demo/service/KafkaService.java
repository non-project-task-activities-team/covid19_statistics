package com.example.demo.service;

import com.example.demo.entity.CovidResponse;
import com.example.demo.kafka.KafkaJsonSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
public class KafkaService {

    public void sendToTopic(List<CovidResponse> countryList) {
        Properties properties = new Properties();
        properties.put("application.id", "covid-19-api-parser");
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        Producer<String, CovidResponse> kafkaProducer = new KafkaProducer<String, CovidResponse>(
                properties,
                new StringSerializer(),
                new KafkaJsonSerializer());
        countryList.forEach(country ->
                kafkaProducer.send(new ProducerRecord<>("countries", "0", country))
        );
    }
}
