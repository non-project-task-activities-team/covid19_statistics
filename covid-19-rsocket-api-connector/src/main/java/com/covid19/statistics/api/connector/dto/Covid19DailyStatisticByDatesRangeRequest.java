package com.covid19.statistics.api.connector.dto;

import java.time.LocalDate;
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
public class Covid19DailyStatisticByDatesRangeRequest {

    private Integer max;
    private LocalDate startDate;
    private LocalDate endDate;
}
