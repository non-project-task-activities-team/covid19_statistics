package com.example.demo.entity;

import com.example.demo.entity.allRequestApi.CountryAll;
import com.example.demo.entity.currentRequestApi.Country;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CovidResponse {

    private static final String DATASOURCE = "covid19api";

    String id;
    String datasource;
    String countryCode;
    String day;
    String confirmed;
    String deaths;
    String recovered;
    String lastModifiedAt;

    public static CovidResponse map(CountryAll country) {
        return CovidResponse.builder()
                .id(UUID.randomUUID().toString())
                .datasource(DATASOURCE)
                .countryCode(country.getCountryCode())
                .day(country.getDate())
                .confirmed(country.getConfirmed())
                .deaths(country.getDeaths())
                .recovered(country.getRecovered())
                .lastModifiedAt(LocalDateTime.now().toString())
                .build();
    }

    public static CovidResponse map(Country country) {
        return CovidResponse.builder()
                .id(UUID.randomUUID().toString())
                .datasource(DATASOURCE)
                .countryCode(country.getCountryCode())
                .day(country.getDate())
                .confirmed(country.getTotalConfirmed())
                .deaths(country.getTotalDeaths())
                .recovered(country.getTotalRecovered())
                .lastModifiedAt(LocalDateTime.now().toString())
                .build();
    }
}
