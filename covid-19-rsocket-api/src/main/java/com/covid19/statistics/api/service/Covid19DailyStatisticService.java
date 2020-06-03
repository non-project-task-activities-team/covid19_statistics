package com.covid19.statistics.api.service;

import com.covid19.statistics.api.dto.Covid19GeneralStatistic;
import java.time.LocalDate;
import reactor.core.publisher.Flux;

public interface Covid19DailyStatisticService {

    Flux<Covid19GeneralStatistic> getCovid19StatisticsByDatesRange(
        LocalDate startDate, LocalDate endDate
    );
}