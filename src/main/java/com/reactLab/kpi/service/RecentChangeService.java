package com.reactLab.kpi.service;

import com.reactLab.kpi.dto.MostActiveDto;
import com.reactLab.kpi.entity.RecentChangeEntry;
import com.reactLab.kpi.repository.RecentChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class RecentChangeService implements RecentChangeServiceApi {
    public static long DAY_IN_MS = 24 * 60 * 60 * 1000L;
    @Autowired
    private RecentChangeRepository recentChangeRepository;

    @Override
    public Mono<RecentChangeEntry> save(RecentChangeEntry e) {
        return recentChangeRepository.save(e);
    }

    @Override
    public Flux<RecentChangeEntry> findAll() {
        return recentChangeRepository.findAll();
    }

    @Override
    public Mono<MostActiveDto> findMostActive() {
        return recentChangeRepository.findMostActive();
    }

    @Override
    public Mono<Void> deleteAll() {
        return recentChangeRepository.deleteAll();
    }
}
