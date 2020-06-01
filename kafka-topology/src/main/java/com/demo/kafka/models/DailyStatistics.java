package com.demo.kafka.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyStatistics {
    private String countryCode;
    private String date;
    private String confirmed;
    private String recovered;
    private String deaths;
}
