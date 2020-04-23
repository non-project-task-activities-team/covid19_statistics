package com.trainingtask.dataprocessor.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Document
public class DailyStatistic {

    @Id
    private String id;

    private String countryCode;
    private LocalDate day;
    private String datasource;

    private Integer infected;
    private Integer deaths;

}
