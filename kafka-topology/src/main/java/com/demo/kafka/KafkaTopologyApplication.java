package com.demo.kafka;

import com.demo.kafka.models.AggregatedCountry;
import com.demo.kafka.models.Country;
import com.demo.kafka.models.DailyStatistics;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootApplication
@EnableBinding(KafkaTopologyApplication.KStreamProcessorWithBranches.class)
public class KafkaTopologyApplication {

    private static final String AMOUNT_STORE_NAME = "amount_store";

    private final ObjectMapper objectMapper;

    public KafkaTopologyApplication(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaTopologyApplication.class, args);
    }

    @StreamListener("countries")
    @SendTo("counts")
    public KStream<?, String> process(KStream<Object, Country> input) {
        return input
                .groupBy((key, value) -> value.getCountryCode())
                .windowedBy(TimeWindows.of(5000))
                .aggregate(this::initialize, this::aggregateAmount)
                .toStream()
                .map((key, value) -> new KeyValue<>(null, value))
                ;




//        KGroupedStream<String, Country> kGroupedStream = input
//                .groupBy((key, value) -> {
//                    log.info("Grouping By Country Code " + value.getCountryCode());
//                    return value.getCountryCode();
//                });
//        KStream<String, AggregatedCountry> aggregatedCountries = aggregate(kGroupedStream);
//        Produced<String, AggregatedCountry> produced = Produced.with(Serdes.String(),
//                new JsonSerde<>(AggregatedCountry.class, objectMapper));
////        aggregatedCountries.to("counts", produced);
//        log.info("Aggregated countries   " + produced.toString());
//        return "aggregatedCountries";

//        return input
//                .groupBy((key, value) -> value.getCountryCode())
//                .aggregate(this::initialize, this::aggregateAmount)
//                .mapValues(value -> {
//                    log.info("TEST " + value.get(0).getConfirmed());
//                    return value.get(0).getConfirmed();
//                })
//                .toStream();
    }

    interface KStreamProcessorWithBranches {

        @Input("countries")
        KStream<?, ?> countries();

        @Output("counts")
        KStream<?, ?> counts();


    }

    //    @Bean
//    public KStream<String, AggregatedCountry> kStream(StreamsBuilder streamsBuilder) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonSerde<Country> countryJsonSerde = new JsonSerde<>(Country.class, objectMapper);
//        KGroupedStream<String, Country> countries = streamsBuilder.stream("countries", Consumed.with(Serdes.String(),
//                countryJsonSerde)).groupByKey(Grouped.with(Serdes.String(), countryJsonSerde));
//        log.info("Countries " + countries.count().toString());
//
//        KStream<String, AggregatedCountry> aggregatedCountries = aggregate(countries);
//
//        Produced<String, AggregatedCountry> produced = Produced.with(Serdes.String(), new JsonSerde<>(AggregatedCountry.class, objectMapper));
//        aggregatedCountries.to("counts", produced);
//        log.info("Aggregated countries   " + produced.toString());
//        return aggregatedCountries;
//    }
//
//    private KStream<String, AggregatedCountry> aggregate(KGroupedStream<String, Country> countries) {
//        return countries.aggregate(this::initialize, this::aggregateAmount)
//                .toStream()
//                .map((key, value) -> new KeyValue<>(null,
//                        AggregatedCountry.builder().dailyStatistics(value).build()));
//    }

    private String aggregateAmount(String key, Country country, String aggregatedAmount) {
        aggregatedAmount = aggregatedAmount.concat(country.getConfirmed());
        log.info("Inside aggregateAmount");
        return aggregatedAmount;
    }

    private String initialize() {
        return "";
    }
//
    private <K, V> Materialized<K, V, KeyValueStore<Bytes, byte[]>> materializedAsPersistentStore(
            String storeName,
            Serde<K> keySerde,
            Serde<V> valueSerde
    ) {
        return Materialized.<K, V>as(Stores.persistentKeyValueStore(storeName))
                .withKeySerde(keySerde)
                .withValueSerde(valueSerde);
    }
}
