package com.trainingtask.dataprocessor.repository;

import com.trainingtask.dataprocessor.model.DailyStatistic;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class MongoRepositoryImpl implements MongoRepository {

    private final MongoTemplate mongoTemplate;

    public MongoRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void upsert(DailyStatistic statistic) {
        Query query = new Query();
        query.addCriteria(
                Criteria.where("country_code").is(statistic.getCountryCode())
                        .and("day").is(statistic.getDay())
                        .and("datasource").is(statistic.getDatasource()));

        Update update = new Update();
        update.set("deaths", statistic.getDeaths());
        update.set("infected", statistic.getInfected());

        mongoTemplate.upsert(query, update, DailyStatistic.class);
    }
}
