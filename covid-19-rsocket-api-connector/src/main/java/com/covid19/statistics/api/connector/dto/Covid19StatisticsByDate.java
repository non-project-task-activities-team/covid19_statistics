package com.covid19.statistics.api.connector.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Covid19StatisticsByDate {

    private UUID id;
    private String countryCode;
    private Integer totalConfirmed;
    private Integer totalDeaths;
    private Integer totalRecovered;
    private LocalDateTime timestamp;
    private LocalDate date;
}
