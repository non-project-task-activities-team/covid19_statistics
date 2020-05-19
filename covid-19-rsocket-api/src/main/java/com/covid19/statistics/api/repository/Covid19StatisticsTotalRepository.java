package com.covid19.statistics.api.repository;

import com.covid19.statistics.api.dto.Covid19StatisticTotal;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface Covid19StatisticsTotalRepository
    extends ReactiveMongoRepository<Covid19StatisticTotal, UUID> {

    Mono<Covid19StatisticTotal> findFirstByOrderByTotalConfirmedDesc();
}
