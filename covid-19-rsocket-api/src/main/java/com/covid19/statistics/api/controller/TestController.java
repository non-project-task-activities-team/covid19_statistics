package com.covid19.statistics.api.controller;

import com.covid19.statistics.api.dto.Covid19StatisticTotal;
import com.covid19.statistics.api.dto.Covid19StatisticsByCountry;
import com.covid19.statistics.api.dto.Covid19StatisticsByCountryRequest;
import com.covid19.statistics.api.repository.Covid19StatisticsByCountryRepository;
import com.covid19.statistics.api.repository.Covid19StatisticsTotalRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class TestController {

    private final Covid19StatisticsTotalRepository statisticsTotalRepo;
    private final Covid19StatisticsByCountryRepository statisticsByCountryRepo;

    @Autowired
    public RSocketController(
      final Covid19StatisticsTotalRepository statisticsTotalRepo,
      final Covid19StatisticsByCountryRepository statisticsByCountryRepo) {
        this.statisticsTotalRepo = statisticsTotalRepo;
        this.statisticsByCountryRepo = statisticsByCountryRepo;
    }

    @GetMapping(path = "/total")
    @ResponseBody
    public Flux<Covid19StatisticTotal> getTotalCovid19Statistics() {
        return statisticsTotalRepo.findAll();
    }

    @GetMapping(path = "/countryList")
    @ResponseBody
    public Flux<Covid19StatisticsByCountry> streamCovid19StatisticsByCountryCodes(
      final List<Covid19StatisticsByCountryRequest> countriesCodes
    ) {
        return statisticsByCountryRepo.getByCountryCode(countriesCodes);
    }

    @GetMapping(path = "/country")
    @ResponseBody
    public Mono<Covid19StatisticsByCountry> streamCovid19StatisticsByCountryCodes(
      final Covid19StatisticsByCountryRequest countryCode
    ) {
        return statisticsByCountryRepo.getByCountryCode(countryCode);
    }

}
