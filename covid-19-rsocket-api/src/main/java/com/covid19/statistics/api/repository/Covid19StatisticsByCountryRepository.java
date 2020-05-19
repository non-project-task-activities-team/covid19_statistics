package com.covid19.statistics.api.repository;

import com.covid19.statistics.api.dto.Covid19StatisticsByCountry;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Covid19StatisticsByCountryRepository
    extends ReactiveMongoRepository<Covid19StatisticsByCountry, UUID> {

    Flux<Covid19StatisticsByCountry> findByCountryCodeIn(List<String> countryCodes);

    Mono<Covid19StatisticsByCountry> findByCountryCode(String countryCode);
}
