package com.covid19.statistics.api.utils.controller;

import com.covid19.statistics.api.utils.dto.Covid19GeneralStatistic;
import com.covid19.statistics.api.utils.dto.ValidationListWrapper;
import com.covid19.statistics.api.utils.repository.Covid19GeneralStatisticRepository;
import com.covid19.statistics.api.utils.service.Covid19StatisticService;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import org.bson.types.ObjectId;
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
@RequestMapping(value = "/covid19-statistics/general", produces = MediaType.APPLICATION_JSON_VALUE)
public class Covid19GeneralStatisticUtilsController {

    private final Covid19StatisticService covid19StatisticService;
    private final Covid19GeneralStatisticRepository covid19GeneralStatisticRepository;

    public Covid19GeneralStatisticUtilsController(
        final Covid19StatisticService covid19StatisticService,
        final Covid19GeneralStatisticRepository covid19GeneralStatisticRepository
    ) {
        this.covid19StatisticService = covid19StatisticService;
        this.covid19GeneralStatisticRepository = covid19GeneralStatisticRepository;
    }

    @GetMapping
    public Flux<Covid19GeneralStatistic> getCovid19GeneralStatistic() {
        return covid19StatisticService.getCovid19GeneralStatistic();
    }

    @GetMapping("/max")
    public Mono<Covid19GeneralStatistic> getMaxCovid19GeneralStatistic() {
        return covid19GeneralStatisticRepository.findFirstByOrderByConfirmedDesc();
    }

    @GetMapping("/{id}")
    public Mono<Covid19GeneralStatistic> getCovid19GeneralStatistic(@PathVariable ObjectId id) {
        return covid19GeneralStatisticRepository.findById(id);
    }

    @GetMapping("/country_code")
    public Mono<Covid19GeneralStatistic> getCovid19GeneralStatisticByCountryCode(
        @RequestParam String countryCode
    ) {
        return covid19GeneralStatisticRepository.findFirstByCountryCode(countryCode);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Covid19GeneralStatistic> saveCovid19GeneralStatistic(
        @Valid @RequestBody Covid19GeneralStatistic covid19GeneralStatistic
    ) {
        covid19GeneralStatistic.setLastModifiedAt(LocalDateTime.now());
        return covid19GeneralStatisticRepository.save(covid19GeneralStatistic);
    }

    @PostMapping(value = "/multiple", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Covid19GeneralStatistic> saveMultipleCovid19GeneralStatistic(
        @Valid @RequestBody ValidationListWrapper<Covid19GeneralStatistic> covid19GeneralStatistic
    ) {
        List<Covid19GeneralStatistic> covid19GeneralStatisticList =
            covid19GeneralStatistic.getData();

        covid19GeneralStatisticList.forEach(s -> s.setLastModifiedAt(LocalDateTime.now()));

        return covid19GeneralStatisticRepository.saveAll(covid19GeneralStatisticList);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Covid19GeneralStatistic> updateCovid19GeneralStatistic(
        @PathVariable String id,
        @Valid @RequestBody Covid19GeneralStatistic covid19GeneralStatistic
    ) {
        covid19GeneralStatistic.setId(id);
        covid19GeneralStatistic.setLastModifiedAt(LocalDateTime.now());
        return covid19GeneralStatisticRepository.save(covid19GeneralStatistic);
    }

    @DeleteMapping
    public Mono<Void> deleteAllCovid19GeneralStatistic() {
        return covid19GeneralStatisticRepository.deleteAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCovid19GeneralStatisticById(@PathVariable ObjectId id) {
        return covid19GeneralStatisticRepository.deleteById(id);
    }
}
