package com.covid19.statistics.api.temporary;

import com.covid19.statistics.api.dto.Covid19StatisticsByCountry;
import com.covid19.statistics.api.repository.Covid19StatisticsByCountryRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
public class MongoInitialFiller {

    @Bean
    ApplicationRunner init(final Covid19StatisticsByCountryRepository repository) {

        final Object[][] data = {
          {"UA", 1000, 100, 10},
          {"BY", 2000, 200, 20},
          {"IT", 3000, 300, 30},
          {"RU", 4000, 400, 40},
          {"USA", 5000, 500, 50}
        };

        return args -> repository
          .deleteAll()
          .thenMany(
            Flux
              .just(data)
              .map(array -> Covid19StatisticsByCountry.builder()
                .countryCode(String.valueOf(array[0]))
                .totalConfirmed((Integer) array[1])
                .totalRecovered((Integer) array[2])
                .totalDeaths((Integer) array[3])
                .build())
              .flatMap(repository::save)
          )
          .thenMany(repository.findAll())
          .subscribe(dto -> System.out.println("saving " + dto.toString()));
    }
}
