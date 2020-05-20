package com.demo.kafka.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AggregatedCountry {

    private String country;
    private String countryCode;
    private Integer totalConfirmed;
    private String totalRecovered;
    private List<DailyStatistics> dailyStatistics;
}
