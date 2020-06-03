package com.covid19.statistics.api.controller;

import static com.mongodb.client.model.changestream.OperationType.INSERT;
import static com.mongodb.client.model.changestream.OperationType.REPLACE;
import static com.mongodb.client.model.changestream.OperationType.UPDATE;

import com.covid19.statistics.api.dto.Covid19GeneralStatistic;
import com.covid19.statistics.api.service.Covid19GeneralStatisticService;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class Covid19GeneralStatisticController {

    private final Covid19GeneralStatisticService covid19GeneralStatisticService;

    public Covid19GeneralStatisticController(
        final Covid19GeneralStatisticService covid19GeneralStatisticService
    ) {
        this.covid19GeneralStatisticService = covid19GeneralStatisticService;
    }

    @MessageMapping("covid19.statistics.general.max")
    public Mono<Covid19GeneralStatistic> getMaxCovid19GeneralStatistic() {
        return covid19GeneralStatisticService.findFirstByOrderByConfirmedDesc();
    }

    @MessageMapping("covid19.statistics.general")
    public Flux<Covid19GeneralStatistic> getCovid19GeneralStatistics() {
        return covid19GeneralStatisticService.findAll();
    }

    @MessageMapping("covid19.statistics.general.stream")
    public Flux<Covid19GeneralStatistic> streamCovid19GeneralStatistics() {
        Flux<Covid19GeneralStatistic> totalFlux = covid19GeneralStatisticService.findAll();

        Flux<ChangeStreamEvent<Covid19GeneralStatistic>> totalFluxStreaming =
            covid19GeneralStatisticService.streamCollectionUpdates(INSERT, UPDATE, REPLACE);

        return totalFlux.concatWith(
            totalFluxStreaming.map(s -> {
                System.out.println(s.getOperationType());
                return s;
            }).map(ChangeStreamEvent::getBody));
    }
}
