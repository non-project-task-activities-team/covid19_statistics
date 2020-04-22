package com.covid19.statistics.api.config;

import com.covid19.statistics.api.dto.Dto;
import com.covid19.statistics.api.repository.DtoRepository;
import java.util.UUID;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
public class ReactiveMongoConfig {

    @Bean
    ApplicationRunner init(final DtoRepository repository) {

        final Object[][] data = {
          {UUID.randomUUID(), "Andrew",},
          {UUID.randomUUID(), "Piranha"},
          {UUID.randomUUID(), "Necky"}
        };

        return args -> repository
          .deleteAll()
          .thenMany(
            Flux
              .just(data)
              .map(array -> Dto.builder()
                .id((UUID) array[0])
                .value(String.valueOf(array[1]))
                .build())
              .flatMap(repository::save)
          )
          .thenMany(repository.findAll())
          .subscribe(dto -> System.out.println("saving " + dto.toString()));
    }
}
