package com.trainingtask.dataprocessor.repository;


import com.trainingtask.dataprocessor.model.DailyStatistic;

@FunctionalInterface
public interface MongoRepository {

    void upsert(DailyStatistic statistic);
}
