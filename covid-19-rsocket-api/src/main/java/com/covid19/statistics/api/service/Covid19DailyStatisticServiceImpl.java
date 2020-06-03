package com.covid19.statistics.api.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import com.covid19.statistics.api.dto.Covid19DailyStatistic;
import com.covid19.statistics.api.dto.Covid19GeneralStatistic;
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
public class Covid19DailyStatisticServiceImpl implements Covid19DailyStatisticService {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Covid19DailyStatisticServiceImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Flux<Covid19GeneralStatistic> getCovid19StatisticsByDatesRange(
        LocalDate startDate, LocalDate endDate
    ) {
        MatchOperation matchOperation =
            match(
                Criteria.where("date").gte(startDate)
                    .andOperator(Criteria.where("date").lte(endDate))
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