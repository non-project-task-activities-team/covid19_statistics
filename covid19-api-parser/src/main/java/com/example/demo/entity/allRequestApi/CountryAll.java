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
public class CountryAll {

     @JsonProperty("Country")
     String country;

     @JsonProperty("CountryCode")
     String countryCode;

     @JsonProperty("Province")
     String province;

     @JsonProperty("City")
     String city;

     @JsonProperty("CityCode")
     String cityCode;

     @JsonProperty("Lat")
     String lat;

     @JsonProperty("Lon")
     String lon;

     @JsonProperty("Confirmed")
     String confirmed;

     @JsonProperty("Deaths")
     String deaths;

     @JsonProperty("Recovered")
     String recovered;

     @JsonProperty("Active")
     String active;

     @JsonProperty("Date")
     String date;
}
