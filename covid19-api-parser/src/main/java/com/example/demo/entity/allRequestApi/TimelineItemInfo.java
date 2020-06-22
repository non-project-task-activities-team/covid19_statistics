package com.example.demo.entity.allRequestApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimelineItemInfo {

    @JsonProperty("new_daily_cases")
    private String  newDailyCases;

    @JsonProperty("new_daily_deaths")
    private String  newDailyDeaths;

    @JsonProperty("total_cases")
    private String  totalCases;

    @JsonProperty("total_recoveries")
    private String  totalRecoveries;

    @JsonProperty("total_deaths")
    private String  totalDeaths;
}
