package com.covid19.statistics.api.utils.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
@Document("general_statistic")
public class Covid19GeneralStatistic {

    @MongoId(value = FieldType.OBJECT_ID)
    private String id;
    @JsonInclude(Include.NON_ABSENT)
    private String datasource;
    @NotBlank
    private String countryCode;
    @NotNull
    private Integer confirmed;
    private Integer deaths;
    private Integer recovered;
    @JsonInclude(Include.NON_ABSENT)
    private LocalDateTime lastModifiedAt;
}
