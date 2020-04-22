package com.covid19.statistics.api.connector.service;

import com.covid19.statistics.api.connector.dto.Covid19StatisticsByCountry;
import com.covid19.statistics.api.connector.dto.Covid19StatisticsByCountryRequest;
import com.covid19.statistics.api.connector.dto.TotalCovid19Statistic;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Covid19StatisticService {

    Flux<TotalCovid19Statistic> getTotalCovid19Statistics(Integer max);

    Flux<Covid19StatisticsByCountry> streamCovid19StatisticsByCountriesCodes(
        List<Covid19StatisticsByCountryRequest> countriesCodes
    );

    Mono<Covid19StatisticsByCountry> findCovid19StatisticsByCountryCode(String countryCode);
}
