package com.covid19.statistics.api.connector.service;

import com.covid19.statistics.api.connector.dto.Covid19StatisticTotal;
import com.covid19.statistics.api.connector.dto.Covid19StatisticsByDateRequest;
import java.time.Duration;
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
                .cache(Duration.ofSeconds(1));
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
    public Mono<Covid19StatisticTotal> getMaxCovid19StatisticTotal() {
        return
            this.requesterMono
                .flatMap(req ->
                    req.route("covid19.statistics.total.max")
                        .retrieveMono(Covid19StatisticTotal.class)
                );
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
    public Flux<Covid19StatisticTotal> getCovid19StatisticsByDatesRange(
        Covid19StatisticsByDateRequest request
    ) {
        return
            this.requesterMono
                .flatMapMany(req ->
                    req.route("covid19.statistics.by.dates.range")
                        .data(request)
                        .retrieveFlux(Covid19StatisticTotal.class));
    }
}
