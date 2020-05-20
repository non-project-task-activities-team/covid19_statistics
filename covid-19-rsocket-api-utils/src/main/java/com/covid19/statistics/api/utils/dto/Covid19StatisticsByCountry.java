package com.covid19.statistics.api.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Covid19StatisticsByCountry {

    private String countryCode;
    private Integer totalConfirmed;
    private Integer totalDeaths;
    private Integer totalRecovered;
}