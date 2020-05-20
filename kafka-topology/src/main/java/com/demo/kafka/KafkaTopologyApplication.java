package com.demo.kafka;

import com.demo.kafka.models.AggregatedCountry;
import com.demo.kafka.models.Country;
import com.demo.kafka.models.DailyStatistics;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication
public class KafkaTopologyApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaTopologyApplication.class, args);
    }

    private static final String AMOUNT_STORE_NAME = "amount_store";

    private KStream<String, AggregatedCountry> aggregate(KGroupedStream<String, Country> countries) {
        ObjectMapper objectMapper = new ObjectMapper();
        Serde<DailyStatistics> dailyStatisticsSerde = new JsonSerde<>(DailyStatistics.class, objectMapper);
        return countries.aggregate(this::initialize, this::aggregateAmount)
                .toStream()
                .map((key, value) -> new KeyValue<>(null,
                        AggregatedCountry.builder().dailyStatistics(value).build()));
    }

    private List<DailyStatistics> aggregateAmount(String key, Country country, List<DailyStatistics> aggregatedAmount) {
        aggregatedAmount.add(DailyStatistics.builder().build());
        return aggregatedAmount;
    }

    private List<DailyStatistics> initialize() {
        return new ArrayList<>();
    }

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
