package com.covid19.statistics.api.temporary;

import com.covid19.statistics.api.dto.Covid19StatisticsByCountry;
import com.covid19.statistics.api.repository.Covid19StatisticsByCountryRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
public class MongoInitialFiller {

//    @Bean
    ApplicationRunner init(final Covid19StatisticsByCountryRepository repository) {

        final Object[][] data = {
          {"UA", 1000, 100, 10, Date.from(LocalDateTime.of(2020, 4, 1, 1, 1).atZone(ZoneId.systemDefault()).toInstant())},
          {"UA", 1000, 100, 10, Date.from(LocalDateTime.of(2020, 4, 5, 2, 1).atZone(ZoneId.systemDefault()).toInstant())},
          {"UA", 1000, 100, 10, Date.from(LocalDateTime.of(2020, 4, 10, 3, 1).atZone(ZoneId.systemDefault()).toInstant())},
          {"UA", 1000, 100, 10, Date.from(LocalDateTime.of(2020, 4, 15, 4, 1).atZone(ZoneId.systemDefault()).toInstant())},
          {"UA", 1000, 100, 10, Date.from(LocalDateTime.of(2020, 4, 20, 5, 1).atZone(ZoneId.systemDefault()).toInstant())},
          {"UA", 1000, 100, 10, Date.from(LocalDateTime.of(2020, 4, 25, 6, 1).atZone(ZoneId.systemDefault()).toInstant())},
          {"UA", 1000, 100, 10, Date.from(LocalDateTime.of(2020, 4, 30, 7, 1).atZone(ZoneId.systemDefault()).toInstant())},
          {"BY", 2000, 200, 20, Date.from(LocalDateTime.of(2020, 4, 25, 8, 1).atZone(ZoneId.systemDefault()).toInstant())},
          {"IT", 3000, 300, 30, Date.from(LocalDateTime.of(2020, 4, 25, 8, 1).atZone(ZoneId.systemDefault()).toInstant())},
          {"RU", 4000, 400, 40, Date.from(LocalDateTime.of(2020, 4, 25, 8, 1).atZone(ZoneId.systemDefault()).toInstant())},
          {"US", 5000, 500, 50, Date.from(LocalDateTime.of(2020, 4, 25, 8, 1).atZone(ZoneId.systemDefault()).toInstant())}
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
                .date((Date) array[4])
                .build())
              .flatMap(repository::save)
          )
          .thenMany(repository.findAll())
          .subscribe(dto -> System.out.println("saving " + dto.toString()));
    }
}
