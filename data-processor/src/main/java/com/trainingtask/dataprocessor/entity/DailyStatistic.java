package com.trainingtask.dataprocessor.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "daily_statistic")
public class DailyStatistic implements Statistic {

    @Id
    private String id;
    private String datasource;
    @Field("country_code")
    private String countryCode;
    private LocalDate day;
    private Integer confirmed;
    private Integer deaths;
    private Integer recovered;
    @Field("last_modified_at")
    private LocalDateTime lastModifiedAt;
}
