package com.covid19.statistics.api.utils.dto;

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
    private UUID id;
    @NotBlank
    private String countryCode;
    @NotNull
    private Integer totalConfirmed;
    private Integer totalDeaths;
    private Integer totalRecovered;
    private LocalDateTime timestamp;
}
