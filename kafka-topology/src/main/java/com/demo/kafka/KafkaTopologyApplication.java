package com.demo.kafka;

import com.demo.kafka.models.Country;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
public class KafkaTopologyApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaTopologyApplication.class, args);
    }

    public static class WordCountProcessorApplication {

        public static final String INPUT_TOPIC = "input";
        public static final String OUTPUT_TOPIC = "output";
        public static final int WINDOW_SIZE_MS = 30000;

        @Bean
        public Function<KStream<Bytes, String>, KStream<Bytes, WordCount>> process() {

            return input -> input
                    .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
                    .map((key, value) -> new KeyValue<>(value, value))
                    .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
                    .windowedBy(TimeWindows.of(Duration.ofMillis(WINDOW_SIZE_MS)))
                    .count(Materialized.as("WordCounts-1"))
                    .toStream()
                    .map((key, value) -> new KeyValue<>(null, new WordCount(key.key(), value, new Date(key.window().start()),
							new Date(key.window().end()))));
        }

        @Bean
        public Consumer<KStream<String, Country>> aggregate() {
            ObjectMapper objectMapper = new ObjectMapper();
            Serde<Country> countrySerde = new JsonSerde<>(Country.class, objectMapper);

            return input -> input
                    .groupBy((s, country) -> country.getCountryCode(), Grouped.with(null, countrySerde))
                    .aggregate(String::new,
                            (s, country, board) -> board.concat(country.getConfirmed()),
                            Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as("test-countries")
                    .withKeySerde(Serdes.String())
                    .withValueSerde(Serdes.String()));
        }
    }

    static class WordCount {

        private String word;

        private long count;

        private Date start;

        private Date end;

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("WordCount{");
            sb.append("word='").append(word).append('\'');
            sb.append(", count=").append(count);
            sb.append(", start=").append(start);
            sb.append(", end=").append(end);
            sb.append('}');
            return sb.toString();
        }

        WordCount() {

        }

        WordCount(String word, long count, Date start, Date end) {
            this.word = word;
            this.count = count;
            this.start = start;
            this.end = end;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public Date getStart() {
            return start;
        }

        public void setStart(Date start) {
            this.start = start;
        }

        public Date getEnd() {
            return end;
        }

        public void setEnd(Date end) {
            this.end = end;
        }
    }
}
