package com.covid19.statistics.api.utils.repository;

import com.covid19.statistics.api.utils.dto.Covid19StatisticsByDate;
import com.covid19.statistics.api.utils.dto.Covid19StatisticsByCountryRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Covid19StatisticByCountryRepository extends ReactiveMongoRepository<Covid19StatisticsByDate, UUID> {

    Flux<Covid19StatisticsByDate> getByCountryCode(List<Covid19StatisticsByCountryRequest> countriesCodes);

    Mono<Covid19StatisticsByDate> getByCountryCode(Covid19StatisticsByCountryRequest countryCode);
}
