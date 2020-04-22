package com.covid19.statistics.api.repository;

import com.covid19.statistics.api.dto.Covid19StatisticsByCountry;
import com.covid19.statistics.api.dto.Covid19StatisticsByCountryRequest;
import com.covid19.statistics.api.dto.TotalCovid19Statistic;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Covid19StatisticRepository {

    Flux<TotalCovid19Statistic> getTotalCovid19Statistics();

    Flux<Covid19StatisticsByCountry> getFindCovid19StatisticsByCountriesCodes(
        List<Covid19StatisticsByCountryRequest> countriesCodes
    );

    Mono<Covid19StatisticsByCountry> findCovid19StatisticsByCountryCode(
        Covid19StatisticsByCountryRequest countryCode
    );
}
