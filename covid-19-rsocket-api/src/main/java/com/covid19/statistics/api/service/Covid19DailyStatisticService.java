package com.covid19.statistics.api.service;

import com.covid19.statistics.api.dto.Covid19DailyStatistic;
import com.covid19.statistics.api.dto.Covid19GeneralStatistic;
import com.mongodb.client.model.changestream.OperationType;
import java.time.LocalDate;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Covid19DailyStatisticService {

    Flux<ChangeStreamEvent<Covid19DailyStatistic>> streamCollectionUpdates(
        OperationType... operationTypes
    );

    Flux<Covid19GeneralStatistic> getCovid19DailyStatisticByDatesRange(
        LocalDate startDate, LocalDate endDate
    );

    Flux<Covid19GeneralStatistic> getCovid19DailyStatisticByDatesRangeAndCountryCode(
        LocalDate startDate, LocalDate endDate, String countryCode
    );
}