package com.example.demo.entity.currentRequestApi;

import com.example.demo.entity.allRequestApi.CovidApiData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CovidApiCurrentData implements CovidApiData {
    @JsonProperty("Global")
    GlobalData global;

    @JsonProperty("Countries")
    List<Country> countries;

    @JsonProperty("Date")
    String date;
}
