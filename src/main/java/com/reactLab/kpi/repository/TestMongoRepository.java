package com.reactLab.kpi.repository;

import com.reactLab.kpi.entity.TestEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TestMongoRepository extends ReactiveMongoRepository<TestEntity,String> {
}
