package com.covid19.statistics.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("general_statistic")
public class Covid19GeneralStatistic {

    @Id
    private UUID id;
    private String datasource;
    private String countryCode;
    private Integer confirmed;
    private Integer deaths;
    private Integer recovered;
    private LocalDateTime lastModifiedAt;
}
