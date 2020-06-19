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
    private int  newDailyCases;

    @JsonProperty("new_daily_deaths")
    private int  newDailyDeaths;

    @JsonProperty("total_cases")
    private int  totalCases;

    @JsonProperty("total_recoveries")
    private int  totalRecoveries;

    @JsonProperty("total_deaths")
    private int  totalDeaths;
}
