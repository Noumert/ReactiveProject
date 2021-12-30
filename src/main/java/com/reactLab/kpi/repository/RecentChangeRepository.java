package com.reactLab.kpi.repository;

import com.reactLab.kpi.dto.MostActiveDto;
import com.reactLab.kpi.entity.RecentChangeEntry;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RecentChangeRepository extends ReactiveCrudRepository<RecentChangeEntry,Long> {
    @Query(
            value = """
                            SELECT user1 as user, count(*) as changes
                            FROM recent_change_entry
                            group by user
                            ORDER BY changes DESC
                            LIMIT 1
                            """
    )
    Mono<MostActiveDto> findMostActive();
}
