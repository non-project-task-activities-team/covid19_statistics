package com.covid19.statistics.api.connector.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Covid19GeneralStatistic {

    @JsonInclude(Include.NON_ABSENT)
    private String id;
    @JsonInclude(Include.NON_ABSENT)
    private String datasource;
    private String countryCode;
    private Integer confirmed;
    private Integer deaths;
    private Integer recovered;
    @JsonInclude(Include.NON_ABSENT)
    private LocalDateTime lastModifiedAt;
}
