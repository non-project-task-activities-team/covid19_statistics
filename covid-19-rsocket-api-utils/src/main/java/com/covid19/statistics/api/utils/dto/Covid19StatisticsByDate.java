package com.covid19.statistics.api.utils.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Covid19StatisticsByDate {

    @Id
    private UUID id;
    private String countryCode;
    private Integer totalConfirmed;
    private Integer totalDeaths;
    private Integer totalRecovered;
    private LocalDateTime timestamp;
    private LocalDate date;
}
