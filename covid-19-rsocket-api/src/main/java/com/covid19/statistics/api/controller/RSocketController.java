package com.covid19.statistics.api.controller;

import com.covid19.statistics.api.dto.Covid19StatisticsByCountry;
import com.covid19.statistics.api.dto.Covid19StatisticsByCountryRequest;
import com.covid19.statistics.api.dto.TotalCovid19Statistic;
import com.covid19.statistics.api.repository.Covid19StatisticRepository;
import java.util.List;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class RSocketController {

    private final Covid19StatisticRepository covid19StatisticRepository;

    public RSocketController(final Covid19StatisticRepository covid19StatisticRepository) {
        this.covid19StatisticRepository = covid19StatisticRepository;
    }

    @MessageMapping("covid19.statistics.total")
    public Flux<TotalCovid19Statistic> getTotalCovid19Statistics() {
        return covid19StatisticRepository.getTotalCovid19Statistics();
    }

    @MessageMapping("covid19.statistics.by.countries")
    public Flux<Covid19StatisticsByCountry> streamCovid19StatisticsByCountriesCodes(
        List<Covid19StatisticsByCountryRequest> countriesCodes
    ) {
        return covid19StatisticRepository.getFindCovid19StatisticsByCountriesCodes(countriesCodes);
    }

    @MessageMapping("covid19.statistics.by.country")
    public Mono<Covid19StatisticsByCountry> streamCovid19StatisticsByCountriesCodes(
        Covid19StatisticsByCountryRequest countryCode
    ) {
        return covid19StatisticRepository.findCovid19StatisticsByCountryCode(countryCode);
    }
}
