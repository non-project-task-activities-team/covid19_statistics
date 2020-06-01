package com.demo.kafka.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AggregatedCountry {

    private String countryCode;
    private Integer totalConfirmed;
    private Integer totalRecovered;
}
