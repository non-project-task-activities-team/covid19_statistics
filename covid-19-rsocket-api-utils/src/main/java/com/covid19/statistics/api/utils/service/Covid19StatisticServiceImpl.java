package com.covid19.statistics.api.utils.service;

import com.covid19.statistics.api.utils.dto.Covid19StatisticTotal;
import java.net.URI;
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
        final @Value("${api.rsocket.websocket.host}") String apiRSocketWebsocketHost,
        final @Value("${api.rsocket.websocket.port}") Integer apiRSocketWebsocketPort,
        final @Value("${api.rsocket.websocket.mapping-path}") String apiRSocketWebsocketMappingPath,
        final @Value("${api.rsocket.websocket.retry}") Integer apiRSocketWebsocketRetry
    ) {
        String uri =
            String.format(
                "ws://%s:%d%s",
                apiRSocketWebsocketHost,
                apiRSocketWebsocketPort,
                apiRSocketWebsocketMappingPath
            );

        this.requesterMono =
            builder
                .dataMimeType(MediaType.valueOf("application/vnd.spring.rsocket.metadata+json"))
                .connectWebSocket(URI.create(uri))
                .retry(apiRSocketWebsocketRetry)
                .cache();
    }

    @Override
    public Flux<Covid19StatisticTotal> getTotalCovid19Statistics() {
        return
            this.requesterMono
                .flatMapMany(req ->
                    req.route("covid19.statistics.total")
                        .retrieveFlux(Covid19StatisticTotal.class));
    }
}
