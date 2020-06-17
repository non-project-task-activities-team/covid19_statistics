package com.trainingtask.dataprocessor.repository;

import com.trainingtask.dataprocessor.entity.DailyStatistic;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class DailyStatisticMongoRepository implements MongoRepository<DailyStatistic> {

    private final MongoTemplate mongoTemplate;

    public DailyStatisticMongoRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void upsert(DailyStatistic dailyStatistic) {
        Query query = new Query();
        query.addCriteria(Criteria
                .where("country_code").is(dailyStatistic.getCountryCode())
                .and("day").is(dailyStatistic.getDay()));

        Update update = new Update();
        update.set("confirmed", dailyStatistic.getConfirmed());
        update.set("deaths", dailyStatistic.getDeaths());
        update.set("recovered", dailyStatistic.getRecovered());
        update.set("last_modified_at", dailyStatistic.getLastModifiedAt());

        mongoTemplate.upsert(query, update, DailyStatistic.class);
    }
}
