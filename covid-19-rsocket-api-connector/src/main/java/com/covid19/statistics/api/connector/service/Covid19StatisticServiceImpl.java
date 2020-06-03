package com.covid19.statistics.api.connector.service;

import com.covid19.statistics.api.connector.dto.Covid19DailyStatisticByDatesRangeRequest;
import com.covid19.statistics.api.connector.dto.Covid19GeneralStatistic;
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
    public Flux<Covid19GeneralStatistic> getCovid19GeneralStatistic() {
        return
            this.requesterMono.flatMapMany(req ->
                req.route("covid19.statistics.general")
                    .retrieveFlux(Covid19GeneralStatistic.class)
            );
    }

    @Override
    public Mono<Covid19GeneralStatistic> getMaxCovid19GeneralStatistic() {
        return
            this.requesterMono.flatMap(req ->
                req.route("covid19.statistics.general.max")
                    .retrieveMono(Covid19GeneralStatistic.class)
            );
    }

    @Override
    public Flux<Covid19GeneralStatistic> streamCovid19GeneralStatistic(Integer max) {
        return
            this.requesterMono.flatMapMany(req ->
                req.route("covid19.statistics.general.stream")
                    .retrieveFlux(Covid19GeneralStatistic.class)
            ).take(max);
    }

    @Override
    public Flux<Covid19GeneralStatistic> streamCovid19DailyStatisticByDatesRange(
        Covid19DailyStatisticByDatesRangeRequest request
    ) {
        return
            this.requesterMono.flatMapMany(req ->
                req.route("covid19.statistics.daily.by_dates_range.stream")
                    .data(request)
                    .retrieveFlux(Covid19GeneralStatistic.class)
            ).take(request.getMax());
    }
}
