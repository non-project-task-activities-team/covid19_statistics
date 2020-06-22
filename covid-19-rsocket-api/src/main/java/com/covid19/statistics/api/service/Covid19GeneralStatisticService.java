package com.covid19.statistics.api.service;

import com.covid19.statistics.api.dto.Covid19GeneralStatistic;
import com.mongodb.client.model.changestream.OperationType;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Covid19GeneralStatisticService {

    Flux<Covid19GeneralStatistic> findAll();

    Mono<Covid19GeneralStatistic> findFirstByOrderByConfirmedDesc();

    Flux<ChangeStreamEvent<Covid19GeneralStatistic>> streamCollectionUpdates(
        OperationType... operationTypes
    );
}
