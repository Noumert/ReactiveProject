package com.reactLab.kpi.service;

import com.reactLab.kpi.dto.MostActiveDto;
import com.reactLab.kpi.dto.MostPopularTitleDto;
import com.reactLab.kpi.dto.RecentChange;
import com.reactLab.kpi.dto.UserInfo;
import com.reactLab.kpi.entity.RecentChangeEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class WikiStatService implements WikiStatServiceApi {
    private final WebClient webClient;
    private final RecentChangeServiceApi recentChangeServiceApi;

    @Autowired
    public WikiStatService(WebClient.Builder builder, RecentChangeServiceApi recentChangeServiceApi) {
        this.webClient = builder.build();
        this.recentChangeServiceApi = recentChangeServiceApi;
    }

    public static long DAY_IN_MS = 24 * 60 * 60 * 1000L;

    @Override
    public Flux<RecentChange> getCurrentChangesStream() {
        return webClient.get()
                .uri("https://stream.wikimedia.org/v2/stream/recentchange")
                .retrieve()
                .bodyToFlux(RecentChange.class)
                .filter(Objects::nonNull)
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofMillis(500)))
                .onErrorContinue((t, e) -> {
                    log.warn("OK error {}", t.getClass());
                });
    }

    @Override
    public Flux<RecentChange> getDayChangesStream(int days) {
        long sinceTimeInMs = System.currentTimeMillis() - DAY_IN_MS * days;
        return webClient.get()
                .uri("https://stream.wikimedia.org/v2/stream/recentchange?since=" + sinceTimeInMs)
                .retrieve()
                .bodyToFlux(RecentChange.class)
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofMillis(500)));
    }

    @Override
    public Flux<UserInfo> getUserStatStream(String user) {
        return webClient.get()
                .uri("https://en.wikipedia.org/w/api.php?action=query&format=json&list=usercontribs&ucuser=" + user)
                .retrieve()
                .bodyToFlux(UserInfo.class)
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofMillis(500)));
    }

    @Override
    public Flux<MostActiveDto> mostActiveUser(Flux<RecentChange> changesDuringDay, int days) {
        return changesDuringDay
//                .filter(r -> r.getMeta().getDt().isAfter(ZonedDateTime.now().minusDays(days)))
                .flatMap(r -> recentChangeServiceApi.save(new RecentChangeEntry(r.getUser(), r.getTitle()
                        , Timestamp.valueOf(r.getMeta().getDt().toLocalDateTime()))))
                .onBackpressureLatest()
                .concatMap(e -> recentChangeServiceApi.findMostActive(), 1)
                .onErrorContinue((t, e) -> {
                });
    }

    @Override
    public Flux<List<MostPopularTitleDto>> mostPopularTitles(Flux<RecentChange> changesDuringDay) {
        return changesDuringDay
//                .filter(r -> r.getMeta().getDt().isAfter(ZonedDateTime.now().minusDays(days)))
                .flatMap(r -> recentChangeServiceApi.save(new RecentChangeEntry(r.getUser(), r.getTitle()
                        , Timestamp.valueOf(r.getMeta().getDt().toLocalDateTime()))))
                .onBackpressureLatest()
                .concatMap(e -> recentChangeServiceApi.findMostPopularTitles(), 1)
                .onErrorContinue((t, e) -> {
                });
    }
}
