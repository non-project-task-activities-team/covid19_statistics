package com.covid19.statistics.api.controller;

import com.covid19.statistics.api.dto.Covid19StatisticTotal;
import com.covid19.statistics.api.dto.Covid19StatisticsByCountry;
import com.covid19.statistics.api.dto.Covid19StatisticsByCountryRequest;
import com.covid19.statistics.api.repository.Covid19StatisticByCountryRepository;
import com.covid19.statistics.api.repository.Covid19StatisticTotalRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class RSocketController {

    private final Covid19StatisticTotalRepository statisticTotalRepo;
    private final Covid19StatisticByCountryRepository statisticByCountryRepo;

    @Autowired
    public RSocketController(
      final Covid19StatisticTotalRepository statisticTotalRepo,
      final Covid19StatisticByCountryRepository statisticByCountryRepo) {
        this.statisticTotalRepo = statisticTotalRepo;
        this.statisticByCountryRepo = statisticByCountryRepo;
    }

    @MessageMapping("covid19.statistics.total")
    public Flux<Covid19StatisticTotal> getTotalCovid19Statistics() {
        return statisticTotalRepo.findAll();
    }

    @MessageMapping("covid19.statistics.by.countries")
    public Flux<Covid19StatisticsByCountry> streamCovid19StatisticsByCountryCodes(
      final List<Covid19StatisticsByCountryRequest> countriesCodes
    ) {
        return statisticByCountryRepo.getByCountryCode(countriesCodes);
    }

    @MessageMapping("covid19.statistics.by.country")
    public Mono<Covid19StatisticsByCountry> streamCovid19StatisticsByCountryCodes(
      final Covid19StatisticsByCountryRequest countryCode
    ) {
        return statisticByCountryRepo.getByCountryCode(countryCode);
    }
}
