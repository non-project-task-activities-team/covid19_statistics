package com.covid19.statistics.api.connector.service;

import com.covid19.statistics.api.connector.dto.Covid19StatisticTotal;
import com.covid19.statistics.api.connector.dto.Covid19StatisticsByCountry;
import com.covid19.statistics.api.connector.dto.Covid19StatisticsByCountryRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class Covid19StatisticServiceImpl implements Covid19StatisticService {

    private final Mono<RSocketRequester> requesterMono;

    public Covid19StatisticServiceImpl(
        final RSocketRequester.Builder builder,
        final @Value("${api.rsocket.tcp.host}") String apiRSocketTcpHost,
        final @Value("${api.rsocket.tcp.port}") Integer apiRSocketTcpPort,
        final @Value("${api.rsocket.tcp.retry}") Integer apiRSocketTcpRetry
    ) {
        this.requesterMono =
            builder
                .dataMimeType(MediaType.APPLICATION_CBOR)
                .connectTcp(apiRSocketTcpHost, apiRSocketTcpPort)
                .retry(apiRSocketTcpRetry)
                .cache();
    }

    @Override
    public Flux<Covid19StatisticTotal> getCovid19StatisticTotal() {
        return
            this.requesterMono
                .flatMapMany(req ->
                    req.route("covid19.statistics.total")
                        .retrieveFlux(Covid19StatisticTotal.class));
    }

    @Override
    public Flux<Covid19StatisticTotal> streamCovid19StatisticTotal(Integer max) {
        return
            this.requesterMono
                .flatMapMany(req ->
                    req.route("covid19.statistics.total.stream")
                        .retrieveFlux(Covid19StatisticTotal.class))
                .take(max);
    }

    @Override
    public Flux<Covid19StatisticsByCountry> streamCovid19StatisticsByCountriesCodes(
        List<Covid19StatisticsByCountryRequest> countriesCodes
    ) {
        return
            this.requesterMono.flatMapMany(req ->
                Flux.fromIterable(countriesCodes)
                    .flatMap(radar ->
                        req.route("covid19.statistics.by.countries")
                            .retrieveFlux(Covid19StatisticsByCountry.class)
                    )
            );
    }

    @Override
    public Mono<Covid19StatisticsByCountry> findCovid19StatisticsByCountryCode(String countryCode) {
        Covid19StatisticsByCountryRequest statisticsByCountryRequest =
            Covid19StatisticsByCountryRequest.builder()
                .countryCode(countryCode)
                .build();

        return
            this.requesterMono
                .flatMap(req ->
                    req.route("covid19.statistics.by.country")
                        .data(statisticsByCountryRequest)
                        .retrieveMono(Covid19StatisticsByCountry.class)
                );
    }
}
