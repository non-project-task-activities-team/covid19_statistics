package com.covid19.statistics.api.repository;

import com.covid19.statistics.api.dto.Covid19StatisticTotal;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface Covid19StatisticTotalRepository extends ReactiveMongoRepository<Covid19StatisticTotal, UUID> {

}
