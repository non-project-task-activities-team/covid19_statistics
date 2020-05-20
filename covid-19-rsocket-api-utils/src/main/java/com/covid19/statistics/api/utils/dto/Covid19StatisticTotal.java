package com.covid19.statistics.api.utils.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@Document("total")
public class Covid19StatisticTotal {

    @Id
    @JsonInclude(Include.NON_ABSENT)
    private UUID id;
    @NotBlank
    private String countryCode;
    @NotNull
    private Integer totalConfirmed;
    private Integer totalDeaths;
    private Integer totalRecovered;
    @JsonInclude(Include.NON_ABSENT)
    private LocalDateTime timestamp;
}
