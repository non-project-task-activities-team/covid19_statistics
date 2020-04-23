package com.demo.kafka.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Country {
    private String country;
    private String countryCode;
    private String confirmed;
    private String recovered;
    private String active;
}
