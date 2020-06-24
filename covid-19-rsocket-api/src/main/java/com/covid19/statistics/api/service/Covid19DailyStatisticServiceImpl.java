package com.covid19.statistics.api.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import com.covid19.statistics.api.dto.Covid19DailyStatistic;
import com.covid19.statistics.api.dto.Covid19GeneralStatistic;
import com.mongodb.client.model.changestream.OperationType;
import java.time.Duration;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class Covid19DailyStatisticServiceImpl implements Covid19DailyStatisticService {

    private static final String OPERATION_TYPE_CRITERIA_KEY = "operationType";

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Covid19DailyStatisticServiceImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Flux<ChangeStreamEvent<Covid19DailyStatistic>> streamCollectionUpdates(
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
            reactiveMongoTemplate.changeStream(Covid19DailyStatistic.class)
                .watchCollection(Covid19DailyStatistic.class)
                .filter(aggregation)
                .listen()
                .cache(Duration.ofSeconds(1));
    }

    private Criteria buildCriteria(OperationType operationType) {
        return new Criteria(OPERATION_TYPE_CRITERIA_KEY).is(operationType.getValue());
    }

    @Override
    public Flux<Covid19GeneralStatistic> getCovid19DailyStatisticByDatesRange(
        LocalDate startDate, LocalDate endDate
    ) {
        MatchOperation matchOperation =
            match(
                Criteria.where("day").gte(startDate)
                    .andOperator(Criteria.where("day").lte(endDate))
            );

        ProjectionOperation projectionOperation =
            project("countryCode", "confirmed", "deaths", "recovered")
                .and("countryCode").previousOperation();

        GroupOperation groupOperation =
            group("countryCode")
                .sum("confirmed").as("confirmed")
                .sum("deaths").as("deaths")
                .sum("recovered").as("recovered");

        Aggregation aggregation =
            newAggregation(
                matchOperation,
                groupOperation,
                projectionOperation
            );

        return
            reactiveMongoTemplate.aggregate(
                aggregation,
                Covid19DailyStatistic.class,
                Covid19GeneralStatistic.class
            );
    }

    @Override
    public Flux<Covid19GeneralStatistic> getCovid19DailyStatisticByDatesRangeAndCountryCode(
        LocalDate startDate, LocalDate endDate, String countryCode
    ) {
        MatchOperation matchOperation =
            match(
                new Criteria().andOperator(
                    Criteria.where("date").gte(startDate),
                    Criteria.where("date").lte(endDate),
                    Criteria.where("countryCode").is(countryCode)
                )
            );

        ProjectionOperation projectionOperation =
            project("countryCode", "confirmed", "deaths", "recovered")
                .and("countryCode").previousOperation();

        GroupOperation groupOperation =
            group("countryCode")
                .sum("confirmed").as("confirmed")
                .sum("deaths").as("deaths")
                .sum("recovered").as("recovered");

        Aggregation aggregation =
            newAggregation(
                matchOperation,
                groupOperation,
                projectionOperation
            );

        return
            reactiveMongoTemplate.aggregate(
                aggregation,
                Covid19DailyStatistic.class,
                Covid19GeneralStatistic.class
            );
    }
}
