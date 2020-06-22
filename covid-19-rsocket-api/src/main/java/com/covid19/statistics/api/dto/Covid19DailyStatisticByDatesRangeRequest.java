package com.covid19.statistics.api.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Covid19DailyStatisticByDatesRangeRequest {

    private LocalDate startDate;
    private LocalDate endDate;
}
