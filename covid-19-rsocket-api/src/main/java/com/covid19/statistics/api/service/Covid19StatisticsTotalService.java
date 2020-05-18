package com.covid19.statistics.api.service;

import com.covid19.statistics.api.dto.Covid19StatisticTotal;
import com.mongodb.client.model.changestream.OperationType;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Covid19StatisticsTotalService {

    Flux<Covid19StatisticTotal> findAll();

    Mono<Covid19StatisticTotal> findFirstByOrderByTotalConfirmedDesc();

    Flux<ChangeStreamEvent<Covid19StatisticTotal>> streamCollectionUpdates(
        OperationType... operationTypes
    );
}
