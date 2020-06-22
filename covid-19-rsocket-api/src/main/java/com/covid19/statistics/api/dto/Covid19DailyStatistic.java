package com.covid19.statistics.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("daily_statistic")
public class Covid19DailyStatistic {

    @MongoId(value = FieldType.OBJECT_ID)
    private String id;
    private String datasource;
    private String countryCode;
    private LocalDate date;
    private Integer confirmed;
    private Integer deaths;
    private Integer recovered;
    private LocalDateTime lastModifiedAt;
}
