package com.reactLab.kpi.service;

import com.reactLab.kpi.entity.TestEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TestServiceApi {
    Mono<TestEntity> create(TestEntity e);

    Flux<TestEntity> findAll();
}
