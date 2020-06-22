package com.covid19.statistics.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
@Document("general_statistic")
public class Covid19GeneralStatistic {

    @JsonInclude(Include.NON_ABSENT)
    @MongoId(value = FieldType.OBJECT_ID)
    private String id;
    private String datasource;
    private String countryCode;
    private Integer confirmed;
    private Integer deaths;
    private Integer recovered;
    @JsonInclude(Include.NON_ABSENT)
    private LocalDateTime lastModifiedAt;
}
