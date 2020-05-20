package com.covid19.statistics.api.connector.controller;

import com.covid19.statistics.api.connector.dto.Covid19StatisticTotal;
import com.covid19.statistics.api.connector.dto.Covid19StatisticsByDateRequest;
import com.covid19.statistics.api.connector.service.Covid19StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
public class Covid19StatisticByDateConnectorController {

    private final Covid19StatisticService covid19StatisticService;

    public Covid19StatisticByDateConnectorController(
        Covid19StatisticService covid19StatisticService
    ) {
        this.covid19StatisticService = covid19StatisticService;
    }

    @MessageMapping("covid19.statistics.by.dates.range")
    public Flux<Covid19StatisticTotal> getCovid19StatisticsByDatesRange(
        Covid19StatisticsByDateRequest request
    ) {
        return this.covid19StatisticService.getCovid19StatisticsByDatesRange(request);
    }
}
