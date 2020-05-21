package com.covid19.statistics.api.utils.controller;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import com.covid19.statistics.api.utils.dto.Covid19StatisticTotal;
import com.covid19.statistics.api.utils.dto.Covid19StatisticsByDate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/covid19-statistics/date", produces = MediaType.APPLICATION_JSON_VALUE)
public class Covid19StatisticByDateUtilsController {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Covid19StatisticByDateUtilsController(
        final ReactiveMongoTemplate reactiveMongoTemplate
    ) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @PostMapping
    public Mono<Covid19StatisticsByDate> streamCovid19StatisticsByDatesRange(
        @RequestBody Covid19StatisticsByDate covid19StatisticsByDate
    ) {
        covid19StatisticsByDate.setId(UUID.randomUUID());
        covid19StatisticsByDate.setTimestamp(LocalDateTime.now());
        return reactiveMongoTemplate.save(covid19StatisticsByDate);
    }

    @GetMapping
    public Flux<Covid19StatisticTotal> streamCovid19StatisticsByDatesRange(
        @RequestParam String startDateStr,
        @RequestParam String endDateStr
    ) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

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
