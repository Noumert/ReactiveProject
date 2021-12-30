package com.reactLab.kpi.repository;

import com.reactLab.kpi.dto.MostActiveDto;
import com.reactLab.kpi.dto.MostPopularTitleDto;
import com.reactLab.kpi.entity.RecentChangeEntry;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RecentChangeRepository extends ReactiveCrudRepository<RecentChangeEntry,Long> {
    @Query(
            value = """
                            SELECT user1, count(*) as changes
                            FROM recent_change_entry
                            group by user1
                            ORDER BY changes DESC
                            LIMIT 1
                            """
    )
    Mono<MostActiveDto> findMostActive();

    @Query(
            value = """                     
                            SELECT title, count(*) as changes
                            FROM recent_change_entry
                            group by title
                            ORDER BY changes DESC
                            LIMIT 10
                            """
    )
    Flux<MostPopularTitleDto> findMostPopularTitles();
}
