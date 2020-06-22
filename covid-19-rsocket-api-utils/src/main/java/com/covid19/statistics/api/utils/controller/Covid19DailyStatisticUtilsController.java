package com.covid19.statistics.api.utils.controller;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import com.covid19.statistics.api.utils.dto.Covid19DailyStatistic;
import com.covid19.statistics.api.utils.dto.Covid19GeneralStatistic;
import com.covid19.statistics.api.utils.dto.ValidationListWrapper;
import com.covid19.statistics.api.utils.repository.Covid19DailyStatisticRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/covid19-statistics/daily", produces = MediaType.APPLICATION_JSON_VALUE)
public class Covid19DailyStatisticUtilsController {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final Covid19DailyStatisticRepository covid19DailyStatisticRepository;

    public Covid19DailyStatisticUtilsController(
        final ReactiveMongoTemplate reactiveMongoTemplate,
        Covid19DailyStatisticRepository covid19DailyStatisticRepository
    ) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.covid19DailyStatisticRepository = covid19DailyStatisticRepository;
    }

    @GetMapping
    public Flux<Covid19DailyStatistic> getAllCovid19DailyStatistic() {
        return covid19DailyStatisticRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Covid19DailyStatistic> getCovid19DailyStatisticById(@PathVariable String id) {
        return covid19DailyStatisticRepository.findById(id);
    }

    @GetMapping("/aggregated")
    public Flux<Covid19GeneralStatistic> getAggregatedCovid19DailyStatisticByDatesRange(
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

    @GetMapping("/aggregated/country_code")
    public Flux<Covid19GeneralStatistic> getAggregatedCovid19DailyStatisticByDatesRangeAndCountryCode(
        @RequestParam String startDateStr,
        @RequestParam String endDateStr,
        @RequestParam String countryCode
    ) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

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

    @PostMapping
    public Mono<Covid19DailyStatistic> saveCovid19DailyStatistic(
        @RequestBody Covid19DailyStatistic covid19DailyStatistic
    ) {
        covid19DailyStatistic.setLastModifiedAt(LocalDateTime.now());
        return covid19DailyStatisticRepository.save(covid19DailyStatistic);
    }

    @PostMapping(value = "/multiple", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Covid19DailyStatistic> saveMultipleCovid19DailyStatistic(
        @Valid @RequestBody ValidationListWrapper<Covid19DailyStatistic> covid19DailyStatistic
    ) {
        List<Covid19DailyStatistic> covid19DailyStatisticList =
            covid19DailyStatistic.getData();

        covid19DailyStatisticList.forEach(s -> s.setLastModifiedAt(LocalDateTime.now()));

        return covid19DailyStatisticRepository.saveAll(covid19DailyStatisticList);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Covid19DailyStatistic> updateCovid19DailyStatistic(
        @PathVariable String id,
        @Valid @RequestBody Covid19DailyStatistic covid19DailyStatistic
    ) {
        covid19DailyStatistic.setId(id);
        covid19DailyStatistic.setLastModifiedAt(LocalDateTime.now());
        return covid19DailyStatisticRepository.save(covid19DailyStatistic);
    }

    @DeleteMapping
    public Mono<Void> deleteAllCovid19DailyStatistic() {
        return covid19DailyStatisticRepository.deleteAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCovid19DailyStatisticById(@PathVariable String id) {
        return covid19DailyStatisticRepository.deleteById(id);
    }
}
