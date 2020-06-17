package com.demo.kafka.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AggregatedCountry {

    private String datasource;
    private String countryCode;
    private Integer confirmed;
    private Integer recovered;
    private Integer deaths;
}
