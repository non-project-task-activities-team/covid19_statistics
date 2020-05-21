package com.covid19.statistics.api.controller;

import com.covid19.statistics.api.dto.Covid19StatisticTotal;
import com.covid19.statistics.api.dto.Covid19StatisticsByDateRequest;
import com.covid19.statistics.api.service.Covid19StatisticsByDateService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class Covid19StatisticByDateController {

    private final Covid19StatisticsByDateService covid19StatisticsByDateService;

    public Covid19StatisticByDateController(
        final Covid19StatisticsByDateService covid19StatisticsByDateService
    ) {
        this.covid19StatisticsByDateService = covid19StatisticsByDateService;
    }

    @MessageMapping("covid19.statistics.by.dates.range")
    public Flux<Covid19StatisticTotal> getCovid19StatisticsByDatesRange(
        final Covid19StatisticsByDateRequest request
    ) {
        return
            covid19StatisticsByDateService.getCovid19StatisticsByDatesRange(
                request.getStartDate(),
                request.getEndDate()
            );
    }
}
