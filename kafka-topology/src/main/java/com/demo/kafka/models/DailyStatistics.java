package com.demo.kafka.models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyStatistics {

    private String datasource;
    private String countryCode;
    private LocalDate day;
    private String confirmed;
    private String recovered;
    private String deaths;
}
