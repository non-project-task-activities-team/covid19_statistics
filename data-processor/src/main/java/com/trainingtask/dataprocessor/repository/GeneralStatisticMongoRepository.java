package com.trainingtask.dataprocessor.repository;

import com.trainingtask.dataprocessor.entity.GeneralStatistic;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class GeneralStatisticMongoRepository implements MongoRepository<GeneralStatistic> {

    private final MongoTemplate mongoTemplate;

    public GeneralStatisticMongoRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void upsert(GeneralStatistic generalStatistic) {
        Query query = new Query();
        query.addCriteria(Criteria.where("country_code").is(generalStatistic.getCountryCode()));

        Update update = new Update();
        update.set("total_deaths", generalStatistic.getTotalDeaths());
        update.set("total_confirmed", generalStatistic.getTotalConfirmed());
        update.set("total_recovered", generalStatistic.getTotalRecovered());
        update.set("last_modified_at", LocalDateTime.now());

        mongoTemplate.upsert(query, update, GeneralStatistic.class);
    }
}
