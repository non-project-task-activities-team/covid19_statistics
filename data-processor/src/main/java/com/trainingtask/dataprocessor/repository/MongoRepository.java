package com.trainingtask.dataprocessor.repository;

import com.trainingtask.dataprocessor.entity.Statistic;

@FunctionalInterface
public interface MongoRepository<T extends Statistic> {

    void upsert(T statistic);
}
