package com.covid19.statistics.api.utils.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotBlank
    private String countryCode;
    @NotNull
    private LocalDate date;
    @NotNull
    private Integer confirmed;
    private Integer deaths;
    private Integer recovered;
    private LocalDateTime lastModifiedAt;
}
