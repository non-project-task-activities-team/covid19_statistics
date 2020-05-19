package com.covid19.statistics.api.utils.repository;

import com.covid19.statistics.api.utils.dto.Covid19StatisticTotal;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface Covid19StatisticTotalRepository
    extends ReactiveMongoRepository<Covid19StatisticTotal, UUID> {

    Mono<Covid19StatisticTotal> findFirstByOrderByTotalConfirmedDesc();
}
