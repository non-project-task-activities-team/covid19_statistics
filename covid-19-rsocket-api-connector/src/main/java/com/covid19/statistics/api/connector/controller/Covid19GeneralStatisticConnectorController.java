package com.covid19.statistics.api.connector.controller;

import com.covid19.statistics.api.connector.dto.Covid19GeneralStatistic;
import com.covid19.statistics.api.connector.dto.Covid19GeneralStatisticRequest;
import com.covid19.statistics.api.connector.service.Covid19StatisticService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class Covid19GeneralStatisticConnectorController {

    private final Covid19StatisticService covid19StatisticService;

    public Covid19GeneralStatisticConnectorController(
        Covid19StatisticService covid19StatisticService
    ) {
        this.covid19StatisticService = covid19StatisticService;
    }

    @MessageMapping("covid19.statistics.total")
    public Flux<Covid19GeneralStatistic> getCovid19GeneralStatistic() {
        return covid19StatisticService.getCovid19GeneralStatistic();
    }

    @MessageMapping("covid19.statistics.total.max")
    public Mono<Covid19GeneralStatistic> getMaxCovid19GeneralStatistic() {
        return covid19StatisticService.getMaxCovid19GeneralStatistic();
    }

    @MessageMapping("covid19.statistics.total.stream")
    public Flux<Covid19GeneralStatistic> streamCovid19GeneralStatistic(
        Covid19GeneralStatisticRequest request
    ) {
        return covid19StatisticService.streamCovid19GeneralStatistic(request.getMax());
    }
}
