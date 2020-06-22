package com.demo.kafka;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootApplication
@EnableBinding(KafkaTopologyApplication.KStreamProcessorWithBranches.class)
public class KafkaTopologyApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaTopologyApplication.class, args);
    }

    @StreamListener("countries1")
    @SendTo("aggregated-statistic")
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

    @StreamListener("countries2")
    @SendTo("daily-statistic")
    public KStream<?, DailyStatistics> daily(KStream<Object, Country> input) {
        return input
                .map((key, value) -> new KeyValue<>(null, DailyStatistics.builder()
                        .datasource(value.getDatasource())
                        .countryCode(value.getCountryCode())
                        .day(value.getDay())
                        .confirmed(value.getConfirmed())
                        .deaths(value.getDeaths())
                        .recovered(value.getRecovered())
                        .build()));
    }

    interface KStreamProcessorWithBranches {
        @Input("countries1")
        KStream<?, ?> countries1();

        @Input("countries2")
        KStream<?, ?> countries2();

        @Output("daily-statistic")
        KStream<?, ?> daily();

        @Output("aggregated-statistic")
        KStream<?, ?> aggregated();

    }

    private AggregatedCountry aggregateAmount(String key, Country country, AggregatedCountry aggregatedCountry) {

        Optional<DailyStatistics> daily = aggregatedCountry.getDailyStatistics().stream()
                .filter(dailyStatistic -> dailyStatistic.getDay().equals(country.getDay()))
                .findFirst();

        if (daily.isPresent()) {
            DailyStatistics statistic = daily.get();
            aggregatedCountry.setConfirmed(aggregatedCountry.getConfirmed() - Integer.parseInt(statistic.getConfirmed())
                    + Integer.parseInt(country.getConfirmed()));
            aggregatedCountry.setRecovered(aggregatedCountry.getRecovered() - Integer.parseInt(statistic.getRecovered())
                    + Integer.parseInt(country.getRecovered()));
            aggregatedCountry.setDeaths(aggregatedCountry.getDeaths() - Integer.parseInt(statistic.getDeaths())
                    + Integer.parseInt(country.getDeaths()));

            statistic.setConfirmed(country.getConfirmed());
            statistic.setRecovered(country.getRecovered());
            statistic.setDeaths(country.getDeaths());

        } else {
            aggregatedCountry.getDailyStatistics().add(DailyStatistics.builder()
                    .confirmed(country.getConfirmed())
                    .deaths(country.getDeaths())
                    .recovered(country.getRecovered())
                    .day(country.getDay())
                    .build());

            aggregatedCountry.setCountryCode(country.getCountryCode());
            aggregatedCountry.setConfirmed(aggregatedCountry.getConfirmed() + Integer.parseInt(country.getConfirmed()));
            aggregatedCountry.setRecovered(aggregatedCountry.getRecovered() + Integer.parseInt(country.getRecovered()));
            aggregatedCountry.setDeaths(aggregatedCountry.getDeaths() + Integer.parseInt(country.getDeaths()));
            aggregatedCountry.setDatasource(aggregatedCountry.getDatasource());
            log.info("Inside aggregateAmount + " + aggregatedCountry);
        }
        return aggregatedCountry;
    }

    private AggregatedCountry initialize() {
        return AggregatedCountry.builder()
                .confirmed(0)
                .recovered(0)
                .deaths(0)
                .dailyStatistics(new ArrayList<>())
                .build();
    }

    private List<DailyStatistics> initializeDailyStatistics() {
        return new ArrayList<>();
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