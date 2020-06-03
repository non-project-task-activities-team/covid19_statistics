package com.covid19.statistics.api.utils.controller;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import com.covid19.statistics.api.utils.dto.Covid19DailyStatistic;
import com.covid19.statistics.api.utils.dto.Covid19GeneralStatistic;
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
@RequestMapping(value = "/covid19-daily-statistic", produces = MediaType.APPLICATION_JSON_VALUE)
public class Covid19DailyStatisticUtilsController {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Covid19DailyStatisticUtilsController(
        final ReactiveMongoTemplate reactiveMongoTemplate
    ) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @PostMapping
    public Mono<Covid19DailyStatistic> streamCovid19DailyStatisticByDatesRange(
        @RequestBody Covid19DailyStatistic covid19DailyStatistic
    ) {
        covid19DailyStatistic.setId(UUID.randomUUID());
        covid19DailyStatistic.setLastModifiedAt(LocalDateTime.now());
        return reactiveMongoTemplate.save(covid19DailyStatistic);
    }

    @GetMapping
    public Flux<Covid19GeneralStatistic> streamCovid19StatisticsByDatesRange(
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
