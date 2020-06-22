package com.covid19.statistics.api.config;

import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class ReactiveMongoConfig {

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(
        final MongoClient mongoClient,
        @Value("${spring.data.mongodb.database}") final String databaseName
    ) {
        return new ReactiveMongoTemplate(mongoClient, databaseName);
    }
}
