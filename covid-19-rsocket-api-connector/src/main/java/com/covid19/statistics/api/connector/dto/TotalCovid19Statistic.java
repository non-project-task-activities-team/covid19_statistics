package com.covid19.statistics.api.connector.dto;

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
public class TotalCovid19Statistic {

    private String countryCode;
    private Integer totalConfirmed;
    private Integer totalDeaths;
    private Integer totalRecovered;
}
