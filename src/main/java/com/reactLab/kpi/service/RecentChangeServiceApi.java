package com.reactLab.kpi.service;

import com.reactLab.kpi.dto.MostActiveDto;
import com.reactLab.kpi.entity.RecentChangeEntry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecentChangeServiceApi {
    Mono<RecentChangeEntry> save(RecentChangeEntry e);

    Flux<RecentChangeEntry> findAll();

    Mono<MostActiveDto> findMostActive();

    Mono<Void> deleteAll();
}
