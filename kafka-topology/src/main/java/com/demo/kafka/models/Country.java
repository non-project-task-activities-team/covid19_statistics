package com.demo.kafka.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    private String datasource;
    private String countryCode;
    private String day;
    private String lastModified;
    private String confirmed;
    private String recovered;
    private String deaths;
}
