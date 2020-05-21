package com.covid19.statistics.api.connector.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.UUID;
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
public class Covid19StatisticTotal {

    @JsonInclude(Include.NON_ABSENT)
    private UUID id;
    private String countryCode;
    private Integer totalConfirmed;
    private Integer totalDeaths;
    private Integer totalRecovered;
    @JsonInclude(Include.NON_ABSENT)
    private LocalDateTime timestamp;
}
