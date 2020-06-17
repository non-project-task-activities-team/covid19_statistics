package com.demo.kafka;
import java.util.Collections;
import java.util.List;

import com.demo.kafka.models.AggregatedCountry;
import com.demo.kafka.models.Country;
import com.demo.kafka.models.DailyStatistics;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.handler.annotation.SendTo;
@Slf4j
@SpringBootApplication
@EnableBinding(KafkaTopologyApplication.KStreamProcessorWithBranches.class)
public class KafkaTopologyApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaTopologyApplication.class, args);
    }
    @StreamListener("countries")
    @SendTo("aggregated")
    public KStream<?, AggregatedCountry> process(KStream<Object, Country> input) {
        return input
                .groupBy((key, value) -> value.getCountryCode())
                .aggregate(this::initialize,
                        this::aggregateAmount,
                        materializedAsPersistentStore("countries", Serdes.String(),
                                Serdes.serdeFrom(new JsonSerializer<>(),
                                        new JsonDeserializer<>(AggregatedCountry.class))))
                .toStream()
                .map((key, value) -> new KeyValue<>(null, value));
    }
    @StreamListener("countries")
    @SendTo("daily")
    public KStream<?, List<DailyStatistics>> daily(KStream<Object, Country> input) {
        return input
                .groupBy((key, value) -> value.getCountryCode())
                .aggregate(this::initializeDailyStatistics,
                        this::dailyStatistics,
                        materializedAsPersistentStore("daily", Serdes.String(),
                                Serdes.serdeFrom(new JsonSerializer<>(),
                                        new JsonDeserializer<>(List.class))))
                .toStream()
                .map((key, value) -> new KeyValue<>(null, value));
    }
    interface KStreamProcessorWithBranches {
        @Input("countries")
        KStream<?, ?> countries();
        @Output("aggregated")
        KStream<?, ?> aggregated();
        @Output("daily")
        KStream<?, ?> daily();
    }
    private AggregatedCountry aggregateAmount(String key, Country country, AggregatedCountry aggregatedCountry) {
        aggregatedCountry.setCountryCode(country.getCountryCode());
        aggregatedCountry.setConfirmed(aggregatedCountry.getConfirmed() + Integer.parseInt(country.getConfirmed()));
        aggregatedCountry.setRecovered(aggregatedCountry.getRecovered() + Integer.parseInt(country.getRecovered()));
        aggregatedCountry.setDeaths(aggregatedCountry.getDeaths() + Integer.parseInt(country.getDeaths()));
        aggregatedCountry.setDatasource(aggregatedCountry.getDatasource());
        log.info("Inside aggregateAmount + " + aggregatedCountry);
        return aggregatedCountry;
    }
    private AggregatedCountry initialize() {
        return AggregatedCountry.builder()
                .confirmed(0)
                .recovered(0)
                .build();
    }
    private List<DailyStatistics> initializeDailyStatistics() {
        return Collections.singletonList(DailyStatistics.builder().build());
    }
    private List<DailyStatistics> dailyStatistics(String key, Country country, List<DailyStatistics> dailyStatistics) {
        dailyStatistics.add(DailyStatistics.builder()
                .countryCode(country.getCountryCode())
                .confirmed(country.getConfirmed())
                .deaths(country.getDeaths())
                .recovered(country.getRecovered())
                .day(country.getDay())
                .datasource(country.getDatasource())
                .build());
        log.info("Inside dailyStatistics + " + dailyStatistics);
        return dailyStatistics;
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