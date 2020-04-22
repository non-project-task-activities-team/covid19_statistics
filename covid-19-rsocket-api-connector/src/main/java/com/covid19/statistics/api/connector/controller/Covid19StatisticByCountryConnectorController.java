package com.covid19.statistics.api.connector.controller;

import com.covid19.statistics.api.connector.config.JsonMetadataStrategiesCustomizer;
import com.covid19.statistics.api.connector.dto.Covid19StatisticsByCountry;
import com.covid19.statistics.api.connector.dto.Covid19StatisticsByCountryRequest;
import com.covid19.statistics.api.connector.service.Covid19StatisticService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class Covid19StatisticByCountryConnectorController {

    private final Covid19StatisticService covid19StatisticService;
    private final Queue<RSocketRequester> connectedClients;

    public Covid19StatisticByCountryConnectorController(
        Covid19StatisticService covid19StatisticService) {
        this.covid19StatisticService = covid19StatisticService;
        this.connectedClients = new ConcurrentLinkedQueue<>();
    }

    @MessageMapping("covid19.statistics.by.countries")
    public Flux<Covid19StatisticsByCountry> streamCovid19StatisticsByCountriesCodes(
        List<Covid19StatisticsByCountryRequest> countriesCodes, RSocketRequester requester
    ) {
        this.connectedClients.offer(requester);
        return
            this.covid19StatisticService.streamCovid19StatisticsByCountriesCodes(countriesCodes)
                .onBackpressureDrop()
                .doOnTerminate(() -> {
                    log.info("Server error while streaming data to the client");
                    this.connectedClients.remove(requester);
                })
                .doOnCancel(() -> {
                    log.info("Connection closed by the client");
                    this.connectedClients.remove(requester);
                });
    }

    @PostMapping("/covid19-statistic/country-code/{countryCode}")
    @ResponseBody
    public Mono<String> sendClientsToLocation(@PathVariable String countryCode) {
        return
            this.covid19StatisticService.findCovid19StatisticsByCountryCode(countryCode)
                .flatMapMany(statistic ->
                    Flux.fromIterable(this.connectedClients)
                        .flatMap(requester -> sendCovid19CountryStatistics(requester, statistic)))
                .then(Mono.just("Sent clients to location " + countryCode));
    }

    private Mono<Void> sendCovid19CountryStatistics(
        RSocketRequester requester,
        Covid19StatisticsByCountry statistics
    ) {
        Map<String, String> metadata = Collections.singletonMap("route", "send.to.location");
        return
            requester.metadata(
                metadata,
                JsonMetadataStrategiesCustomizer.METADATA_MIME_TYPE
            )
                .data(statistics)
                .send();
    }
}
