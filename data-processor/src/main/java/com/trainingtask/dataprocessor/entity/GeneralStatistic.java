package com.trainingtask.dataprocessor.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "general_statistic")
public class GeneralStatistic implements Statistic {

    @Id
    private String id;
    @Field("country_code")
    @Indexed(name = "country_code_index", direction = IndexDirection.DESCENDING)
    private String countryCode;
    private String datasource;
    @Field("total_confirmed")
    private Integer totalConfirmed;
    @Field("total_deaths")
    private Integer totalDeaths;
    @Field("total_recovered")
    private Integer totalRecovered;
    @Field("last_modified_at")
    private LocalDateTime lastModifiedAt;
}
