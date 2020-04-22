package com.covid19.statistics.api.controller;

import com.covid19.statistics.api.dto.Covid19StatisticsByCountry;
import com.covid19.statistics.api.dto.Covid19StatisticsByCountryRequest;
import com.covid19.statistics.api.repository.Covid19StatisticsByCountryRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class Covid19StatisticByCountryController {

    private final Covid19StatisticsByCountryRepository statisticsByCountryRepo;

    public Covid19StatisticByCountryController(
        final Covid19StatisticsByCountryRepository statisticsByCountryRepo
    ) {
        this.statisticsByCountryRepo = statisticsByCountryRepo;
    }

    @MessageMapping("covid19.statistics.by.countries")
    public Flux<Covid19StatisticsByCountry> streamCovid19StatisticsByCountryCodes(
        final List<Covid19StatisticsByCountryRequest> countryCodes
    ) {
        List<String> codes = countryCodes.stream()
            .map(Covid19StatisticsByCountryRequest::getCountryCode)
            .collect(Collectors.toList());
        return statisticsByCountryRepo.findByCountryCodeIn(codes);
    }

    @MessageMapping("covid19.statistics.by.country")
    public Mono<Covid19StatisticsByCountry> streamCovid19StatisticsByCountryCodes(
        final Covid19StatisticsByCountryRequest countryCode
    ) {
        return statisticsByCountryRepo.findByCountryCode(countryCode.getCountryCode());
    }
}
