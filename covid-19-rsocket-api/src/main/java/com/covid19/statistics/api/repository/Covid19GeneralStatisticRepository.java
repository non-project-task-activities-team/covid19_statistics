package com.covid19.statistics.api.repository;

import com.covid19.statistics.api.dto.Covid19GeneralStatistic;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface Covid19GeneralStatisticRepository
    extends ReactiveMongoRepository<Covid19GeneralStatistic, UUID> {

    Mono<Covid19GeneralStatistic> findFirstByOrderByConfirmedDesc();
}
