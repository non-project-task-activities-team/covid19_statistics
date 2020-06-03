package com.covid19.statistics.api.utils.repository;

import com.covid19.statistics.api.utils.dto.Covid19DailyStatistic;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface Covid19DailyStatisticRepository
    extends ReactiveMongoRepository<Covid19DailyStatistic, UUID> {

}
