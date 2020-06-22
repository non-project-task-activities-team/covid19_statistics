package com.trainingtask.dataprocessor.repository;

import com.trainingtask.dataprocessor.entity.DailyStatistic;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

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
                .where("countryCode").is(dailyStatistic.getCountryCode().trim())
                .and("day").is(dailyStatistic.getDay()));

        Update update = new Update();
        update.set("confirmed", dailyStatistic.getConfirmed());
        update.set("deaths", dailyStatistic.getDeaths());
        update.set("recovered", dailyStatistic.getRecovered());
        update.set("lastModifiedAt", LocalDateTime.now());

        mongoTemplate.upsert(query, update, DailyStatistic.class);
    }
}
