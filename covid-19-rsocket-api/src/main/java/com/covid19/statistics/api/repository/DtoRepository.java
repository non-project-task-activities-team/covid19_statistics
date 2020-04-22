package com.covid19.statistics.api.repository;

import com.covid19.statistics.api.dto.Dto;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DtoRepository extends ReactiveMongoRepository<Dto, UUID> {

}
