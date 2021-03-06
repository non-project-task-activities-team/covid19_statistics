package com.example.demo.entity.currentRequestApi;

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
public class GlobalData {

    @JsonProperty("NewConfirmed")
    String newConfirmed;
    @JsonProperty("TotalConfirmed")
    String totalConfirmed;
    @JsonProperty("NewDeaths")
    String newDeaths;
    @JsonProperty("TotalDeaths")
    String totalDeaths;
    @JsonProperty("NewRecovered")
    String newRecovered;
    @JsonProperty("TotalRecovered")
    String totalRecovered;
}
