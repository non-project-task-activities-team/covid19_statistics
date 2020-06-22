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
        query.addCriteria(Criteria.where("countryCode").is(generalStatistic.getCountryCode().trim()));

        Update update = new Update();
        update.set("deaths", generalStatistic.getDeaths());
        update.set("confirmed", generalStatistic.getConfirmed());
        update.set("recovered", generalStatistic.getRecovered());
        update.set("lastModifiedAt", LocalDateTime.now());

        mongoTemplate.upsert(query, update, GeneralStatistic.class);
    }
}
