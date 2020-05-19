package com.covid19.statistics.api.utils.repository;

import com.covid19.statistics.api.utils.dto.Covid19StatisticsByCountry;
import com.covid19.statistics.api.utils.dto.Covid19StatisticsByCountryRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Covid19StatisticByCountryRepository extends ReactiveMongoRepository<Covid19StatisticsByCountry, UUID> {

    Flux<Covid19StatisticsByCountry> getByCountryCode(List<Covid19StatisticsByCountryRequest> countriesCodes);

    Mono<Covid19StatisticsByCountry> getByCountryCode(Covid19StatisticsByCountryRequest countryCode);
}
