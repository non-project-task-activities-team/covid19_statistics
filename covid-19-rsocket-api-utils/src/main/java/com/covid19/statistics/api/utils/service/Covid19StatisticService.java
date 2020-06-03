package com.covid19.statistics.api.utils.service;

import com.covid19.statistics.api.utils.dto.Covid19GeneralStatistic;
import reactor.core.publisher.Flux;

public interface Covid19StatisticService {

    Flux<Covid19GeneralStatistic> getCovid19GeneralStatistic();
}
