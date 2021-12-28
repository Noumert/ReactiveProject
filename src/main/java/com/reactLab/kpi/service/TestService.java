package com.reactLab.kpi.service;

import com.reactLab.kpi.entity.TestEntity;
import com.reactLab.kpi.repository.TestMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TestService implements TestServiceApi {
    @Autowired
    private TestMongoRepository testMongoRepository;

    public Mono<TestEntity> create(TestEntity e) {
        return testMongoRepository.save(e);
    }

    public Flux<TestEntity> findAll() {
        return testMongoRepository.findAll();
    }
}
