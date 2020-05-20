package com.demo.kafka.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyStatistics {
    private String date;
    private String confirmed;
    private String recovered;
    private String death;
}
