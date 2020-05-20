package com.covid19.statistics.api.service;

import com.covid19.statistics.api.dto.Covid19StatisticTotal;
import java.time.LocalDate;
import reactor.core.publisher.Flux;

public interface Covid19StatisticsByDateService {

    Flux<Covid19StatisticTotal> getCovid19StatisticsByDatesRange(
        LocalDate startDate, LocalDate endDate
    );
}