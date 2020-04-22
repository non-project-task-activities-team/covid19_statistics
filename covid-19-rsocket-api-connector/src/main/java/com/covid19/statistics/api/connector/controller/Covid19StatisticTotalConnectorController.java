package com.covid19.statistics.api.connector.controller;

import com.covid19.statistics.api.connector.dto.Covid19StatisticTotal;
import com.covid19.statistics.api.connector.dto.Covid19StatisticTotalRequest;
import com.covid19.statistics.api.connector.service.Covid19StatisticService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class Covid19StatisticTotalConnectorController {

    private final Covid19StatisticService covid19StatisticService;

    public Covid19StatisticTotalConnectorController(
        Covid19StatisticService covid19StatisticService
    ) {
        this.covid19StatisticService = covid19StatisticService;
    }

    @MessageMapping("covid19.statistics.total")
    public Flux<Covid19StatisticTotal> getCovid19StatisticTotals() {
        return covid19StatisticService.getCovid19StatisticTotal();
    }

    @MessageMapping("covid19.statistics.total.stream")
    public Flux<Covid19StatisticTotal> streamCovid19StatisticTotals(
        Covid19StatisticTotalRequest request
    ) {
        return covid19StatisticService.streamCovid19StatisticTotal(request.getMax());
    }
}
