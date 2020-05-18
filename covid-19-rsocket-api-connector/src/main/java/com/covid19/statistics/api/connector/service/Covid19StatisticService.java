package com.covid19.statistics.api.connector.service;

import com.covid19.statistics.api.connector.dto.Covid19StatisticTotal;
import com.covid19.statistics.api.connector.dto.Covid19StatisticsByCountry;
import com.covid19.statistics.api.connector.dto.Covid19StatisticsByCountryRequest;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Covid19StatisticService {

    Flux<Covid19StatisticTotal> getCovid19StatisticTotal();

    Mono<Covid19StatisticTotal> getMaxCovid19StatisticTotal();

    Flux<Covid19StatisticTotal> streamCovid19StatisticTotal(Integer max);

    Flux<Covid19StatisticsByCountry> streamCovid19StatisticsByCountriesCodes(
        List<Covid19StatisticsByCountryRequest> countriesCodes
    );

    Mono<Covid19StatisticsByCountry> findCovid19StatisticsByCountryCode(String countryCode);
}
