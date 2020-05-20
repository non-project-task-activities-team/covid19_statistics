package com.covid19.statistics.api.controller;

import static com.mongodb.client.model.changestream.OperationType.INSERT;
import static com.mongodb.client.model.changestream.OperationType.REPLACE;
import static com.mongodb.client.model.changestream.OperationType.UPDATE;

import com.covid19.statistics.api.dto.Covid19StatisticTotal;
import com.covid19.statistics.api.service.Covid19StatisticsTotalService;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class Covid19StatisticTotalController {

    private final Covid19StatisticsTotalService covid19StatisticsTotalService;

    public Covid19StatisticTotalController(
        final Covid19StatisticsTotalService covid19StatisticsTotalService
    ) {
        this.covid19StatisticsTotalService = covid19StatisticsTotalService;
    }

    @MessageMapping("covid19.statistics.total.max")
    public Mono<Covid19StatisticTotal> getMaxCovid19StatisticTotal() {
        return covid19StatisticsTotalService.findFirstByOrderByTotalConfirmedDesc();
    }

    @MessageMapping("covid19.statistics.total")
    public Flux<Covid19StatisticTotal> getCovid19StatisticTotals() {
        return covid19StatisticsTotalService.findAll();
    }

    @MessageMapping("covid19.statistics.total.stream")
    public Flux<Covid19StatisticTotal> streamCovid19StatisticTotals() {
        Flux<Covid19StatisticTotal> totalFlux = covid19StatisticsTotalService.findAll();

        Flux<ChangeStreamEvent<Covid19StatisticTotal>> totalFluxStreaming =
            covid19StatisticsTotalService.streamCollectionUpdates(INSERT, UPDATE, REPLACE);

        return totalFlux.concatWith(
            totalFluxStreaming.map(s -> {
                System.out.println(s.getOperationType());
                return s;
            }).map(ChangeStreamEvent::getBody));
    }
}
