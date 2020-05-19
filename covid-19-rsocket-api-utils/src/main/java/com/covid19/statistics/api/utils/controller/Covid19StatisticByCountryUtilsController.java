package com.covid19.statistics.api.utils.controller;

import com.covid19.statistics.api.utils.dto.Covid19StatisticTotal;
import com.covid19.statistics.api.utils.repository.Covid19StatisticTotalRepository;
import com.covid19.statistics.api.utils.service.Covid19StatisticService;
import java.util.UUID;
import javax.validation.Valid;
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
@RequestMapping(value = "/covid19-statistics/country", produces = MediaType.APPLICATION_JSON_VALUE)
public class Covid19StatisticByCountryUtilsController {

    private final Covid19StatisticService covid19StatisticService;
    private final Covid19StatisticTotalRepository covid19StatisticTotalRepository;

    public Covid19StatisticByCountryUtilsController(
        final Covid19StatisticService covid19StatisticService,
        final Covid19StatisticTotalRepository covid19StatisticTotalRepository
    ) {
        this.covid19StatisticService = covid19StatisticService;
        this.covid19StatisticTotalRepository = covid19StatisticTotalRepository;
    }

//    @MessageMapping("covid19.statistics.by.countries")
//    public Flux<Covid19StatisticsByCountry> streamCovid19StatisticsByCountryCodes(
//      final List<Covid19StatisticsByCountryRequest> countriesCodes
//    ) {
//        return covid19StatisticService.getByCountryCode(countriesCodes);
//    }

//    @MessageMapping("covid19.statistics.by.country")
//    public Mono<Covid19StatisticsByCountry> streamCovid19StatisticsByCountryCodes(
//      final Covid19StatisticsByCountryRequest countryCode
//    ) {
//        return covid19StatisticService.getByCountryCode(countryCode);
//    }
}
