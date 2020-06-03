package com.covid19.statistics.api.connector.service;

import com.covid19.statistics.api.connector.dto.Covid19GeneralStatistic;
import com.covid19.statistics.api.connector.dto.Covid19DailyStatisticByDatesRangeRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Covid19StatisticService {

    Flux<Covid19GeneralStatistic> getCovid19GeneralStatistic();

    Mono<Covid19GeneralStatistic> getMaxCovid19GeneralStatistic();

    Flux<Covid19GeneralStatistic> streamCovid19GeneralStatistic(Integer max);

    Flux<Covid19GeneralStatistic> streamCovid19DailyStatisticByDatesRange(
        Covid19DailyStatisticByDatesRangeRequest request
    );
}
