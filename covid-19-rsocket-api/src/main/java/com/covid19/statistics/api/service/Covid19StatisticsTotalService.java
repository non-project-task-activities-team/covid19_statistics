package com.covid19.statistics.api.service;

import com.covid19.statistics.api.dto.Covid19StatisticTotal;
import com.mongodb.client.model.changestream.OperationType;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import reactor.core.publisher.Flux;

public interface Covid19StatisticsTotalService {

    Flux<Covid19StatisticTotal> findAll();

    Flux<ChangeStreamEvent<Covid19StatisticTotal>> streamCollectionUpdates(
        OperationType... operationTypes
    );
}
