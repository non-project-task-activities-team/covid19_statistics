package com.covid19.statistics.api.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import com.covid19.statistics.api.dto.Covid19StatisticTotal;
import com.covid19.statistics.api.dto.Covid19StatisticsByDate;
import java.time.LocalDate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class Covid19StatisticsByDateServiceImpl implements Covid19StatisticsByDateService {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Covid19StatisticsByDateServiceImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Flux<Covid19StatisticTotal> getCovid19StatisticsByDatesRange(
        LocalDate startDate, LocalDate endDate
    ) {
        MatchOperation matchOperation =
            match(
                Criteria.where("date").gte(startDate)
                    .andOperator(Criteria.where("date").lte(endDate))
            );

        ProjectionOperation projectionOperation =
            project("countryCode", "totalConfirmed", "totalDeaths", "totalRecovered")
                .and("countryCode").previousOperation();

        GroupOperation groupOperation =
            group("countryCode")
                .sum("totalConfirmed").as("totalConfirmed")
                .sum("totalDeaths").as("totalDeaths")
                .sum("totalRecovered").as("totalRecovered");

        Aggregation aggregation =
            newAggregation(
                matchOperation,
                groupOperation,
                projectionOperation
            );

        return
            reactiveMongoTemplate.aggregate(
                aggregation,
                Covid19StatisticsByDate.class,
                Covid19StatisticTotal.class
            );
    }
}