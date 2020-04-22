package com.covid19.statistics.api.repository;

import com.covid19.statistics.api.dto.Covid19StatisticsByCountry;
import com.covid19.statistics.api.dto.Covid19StatisticsByCountryRequest;
import com.covid19.statistics.api.dto.TotalCovid19Statistic;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class Covid19StatisticRepositoryImpl implements Covid19StatisticRepository {

    private final Random random;
    private final int randomLow;
    private final int randomHigh;

    public Covid19StatisticRepositoryImpl() {
        this.random = new Random();
        this.randomLow = 0;
        this.randomHigh = 50;
    }

    @Override
    public Flux<TotalCovid19Statistic> getTotalCovid19Statistics() {
        // TODO

        return
            Flux.fromStream(
                Stream.generate(this::getRandomValue)
                    .map(value ->
                        TotalCovid19Statistic.builder()
                            .countryCode("UA")
                            .totalConfirmed(value)
                            .build()
                    )
            ).delayElements(Duration.ofMillis(500));
    }

    @Override
    public Flux<Covid19StatisticsByCountry> getFindCovid19StatisticsByCountriesCodes(
        List<Covid19StatisticsByCountryRequest> countriesCodes
    ) {
        // TODO
        return
            Flux.fromStream(
                Stream.generate(this::getRandomValue)
                    .map(value ->
                        Covid19StatisticsByCountry.builder()
                            .countryCode("UA")
                            .totalConfirmed(value)
                            .build()
                    )
            ).delayElements(Duration.ofMillis(500));
    }

    @Override
    public Mono<Covid19StatisticsByCountry> findCovid19StatisticsByCountryCode(
        Covid19StatisticsByCountryRequest countryCode
    ) {
        // TODO
        return Mono.just(
            Covid19StatisticsByCountry.builder()
                .countryCode("UA")
                .totalConfirmed(getRandomValue())
                .build()
        );
    }

    private Integer getRandomValue() {
        return random.nextInt(randomHigh - randomLow) + randomLow;
    }
}
