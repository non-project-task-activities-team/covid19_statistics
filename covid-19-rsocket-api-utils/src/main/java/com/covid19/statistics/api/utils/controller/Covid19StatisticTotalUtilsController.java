package com.covid19.statistics.api.utils.controller;

import com.covid19.statistics.api.utils.dto.Covid19StatisticTotal;
import com.covid19.statistics.api.utils.dto.ValidationListWrapper;
import com.covid19.statistics.api.utils.repository.Covid19StatisticTotalRepository;
import com.covid19.statistics.api.utils.service.Covid19StatisticService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/covid19-statistics/total", produces = MediaType.APPLICATION_JSON_VALUE)
public class Covid19StatisticTotalUtilsController {

    private final Covid19StatisticService covid19StatisticService;
    private final Covid19StatisticTotalRepository covid19StatisticTotalRepository;

    public Covid19StatisticTotalUtilsController(
        final Covid19StatisticService covid19StatisticService,
        final Covid19StatisticTotalRepository covid19StatisticTotalRepository
    ) {
        this.covid19StatisticService = covid19StatisticService;
        this.covid19StatisticTotalRepository = covid19StatisticTotalRepository;
    }

    @GetMapping
    public Flux<Covid19StatisticTotal> getTotalCovid19Statistics() {
        return covid19StatisticService.getTotalCovid19Statistics();
    }

    @GetMapping("/max")
    public Mono<Covid19StatisticTotal> getMaxTotalCovid19Statistics() {
        return covid19StatisticTotalRepository.findFirstByOrderByTotalConfirmedDesc();
    }

    @GetMapping("/{id}")
    public Mono<Covid19StatisticTotal> getTotalCovid19Statistics(
        @PathVariable UUID id
    ) {
        return covid19StatisticTotalRepository.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Covid19StatisticTotal> saveCovid19StatisticTotal(
        @Valid @RequestBody Covid19StatisticTotal covid19StatisticTotal
    ) {
        covid19StatisticTotal.setId(UUID.randomUUID());
        covid19StatisticTotal.setTimestamp(LocalDateTime.now());
        return covid19StatisticTotalRepository.save(covid19StatisticTotal);
    }

    @PostMapping(value = "/multiple", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Covid19StatisticTotal> saveMultipleCovid19StatisticTotal(
        @Valid @RequestBody ValidationListWrapper<Covid19StatisticTotal> covid19StatisticTotals
    ) {
        List<Covid19StatisticTotal> covid19StatisticTotalsList = covid19StatisticTotals.getData();
        covid19StatisticTotalsList.forEach(s -> {
            s.setId(UUID.randomUUID());
            s.setTimestamp(LocalDateTime.now());
        });
        return covid19StatisticTotalRepository.saveAll(covid19StatisticTotalsList);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Covid19StatisticTotal> updateCovid19StatisticTotal(
        @PathVariable UUID id,
        @Valid @RequestBody Covid19StatisticTotal covid19StatisticTotal
    ) {
        covid19StatisticTotal.setId(id);
        covid19StatisticTotal.setTimestamp(LocalDateTime.now());
        return covid19StatisticTotalRepository.save(covid19StatisticTotal);
    }

    @DeleteMapping
    public Mono<Void> deleteAllCovid19StatisticTotals() {
        return covid19StatisticTotalRepository.deleteAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCovid19StatisticTotalById(@PathVariable UUID id) {
        return covid19StatisticTotalRepository.deleteById(id);
    }
}
