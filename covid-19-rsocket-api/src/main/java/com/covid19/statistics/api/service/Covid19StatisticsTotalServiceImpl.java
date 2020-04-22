package com.covid19.statistics.api.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import com.covid19.statistics.api.dto.Covid19StatisticTotal;
import com.covid19.statistics.api.repository.Covid19StatisticsTotalRepository;
import com.mongodb.client.model.changestream.OperationType;
import java.util.stream.Stream;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class Covid19StatisticsTotalServiceImpl implements Covid19StatisticsTotalService {

    private static final String OPERATION_TYPE_CRITERIA_KEY = "operationType";

    private final Covid19StatisticsTotalRepository covid19StatisticsTotalRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Covid19StatisticsTotalServiceImpl(
        Covid19StatisticsTotalRepository covid19StatisticsTotalRepository,
        ReactiveMongoTemplate reactiveMongoTemplate
    ) {
        this.covid19StatisticsTotalRepository = covid19StatisticsTotalRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Flux<Covid19StatisticTotal> findAll() {
        return covid19StatisticsTotalRepository.findAll();
    }

    @Override
    public Flux<ChangeStreamEvent<Covid19StatisticTotal>> streamCollectionUpdates(
        OperationType... operationTypes
    ) {
        if (operationTypes == null || operationTypes.length == 0) {
            throw new IllegalArgumentException("Operation Type(s) not provided!");
        }

        Criteria[] operationTypesCriteria =
            Stream.of(operationTypes)
                .map(this::buildCriteria)
                .toArray(Criteria[]::new);

        Criteria criteria = new Criteria().orOperator(operationTypesCriteria);
        MatchOperation matchOperation = new MatchOperation(criteria);
        Aggregation aggregation = newAggregation(matchOperation);

        return
            reactiveMongoTemplate.changeStream(Covid19StatisticTotal.class)
                .watchCollection(Covid19StatisticTotal.class)
                .filter(aggregation)
                .listen();
    }

    private Criteria buildCriteria(OperationType operationType) {
        return new Criteria(OPERATION_TYPE_CRITERIA_KEY).is(operationType.getValue());
    }
}
