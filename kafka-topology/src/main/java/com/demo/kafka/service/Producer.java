package com.demo.kafka.service;

import com.demo.kafka.models.Country;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "countries";

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public Producer(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(List<Country> countries) {
        countries.forEach(country -> {
            try {
                String message = objectMapper.writeValueAsString(country);
                logger.info(String.format("#### -> Producing countries -> %s", message));
                this.kafkaTemplate.send(TOPIC, message);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
