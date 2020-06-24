package com.trainingtask.dataprocessor.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "daily_statistic")
@CompoundIndexes({
        @CompoundIndex(def = "{'day':-1, 'countryCode':-1}", name = "compound_index")
})
public class DailyStatistic implements Statistic {

    @Id
    private String id;
    private String countryCode;
    private LocalDate day;
    private String datasource;
    private Integer confirmed;
    private Integer deaths;
    private Integer recovered;
    private LocalDateTime lastModifiedAt;
}
