package com.covid19.statistics.api.connector.controller;

import com.covid19.statistics.api.connector.dto.Covid19GeneralStatistic;
import com.covid19.statistics.api.connector.dto.Covid19DailyStatisticByDatesRangeRequest;
import com.covid19.statistics.api.connector.service.Covid19StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
public class Covid19DailyStatisticConnectorController {

    private final Covid19StatisticService covid19StatisticService;

    public Covid19DailyStatisticConnectorController(
        Covid19StatisticService covid19StatisticService
    ) {
        this.covid19StatisticService = covid19StatisticService;
    }

    @MessageMapping("covid19.statistics.daily.by_dates_range.stream")
    public Flux<Covid19GeneralStatistic> streamCovid19DailyStatisticByDatesRange(
        Covid19DailyStatisticByDatesRangeRequest request
    ) {
        return this.covid19StatisticService.streamCovid19DailyStatisticByDatesRange(request);
    }
}
