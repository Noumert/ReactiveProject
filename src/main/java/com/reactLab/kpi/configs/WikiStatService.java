package com.reactLab.kpi.configs;

import com.reactLab.kpi.dto.MostActiveDto;
import com.reactLab.kpi.dto.RecentChange;
import com.reactLab.kpi.dto.UserInfo;
import com.reactLab.kpi.entity.RecentChangeEntry;
import com.reactLab.kpi.service.RecentChangeServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class WikiStatService {
    private final WebClient webClient;
    private final RecentChangeServiceApi recentChangeServiceApi;

    @Autowired
    public WikiStatService(WebClient.Builder builder, RecentChangeServiceApi recentChangeServiceApi) {
        this.webClient = builder.build();
        this.recentChangeServiceApi = recentChangeServiceApi;
    }
    public static long DAY_IN_MS= 24 * 60 * 60 * 1000L;


    public Flux<UserInfo> getUserStatStream(String user) {
        return webClient.get()
                .uri("https://en.wikipedia.org/w/api.php?action=query&format=json&list=usercontribs&ucuser="+user)
                .retrieve()
                .bodyToFlux(UserInfo.class)
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofMillis(500)));
    }

    public Flux<MostActiveDto> mostActiveUser(int days) {
        return webClient.get()
                .uri("https://stream.wikimedia.org/v2/stream/recentchange?since" + (System.currentTimeMillis()-DAY_IN_MS*days))
                .retrieve()
                .bodyToFlux(RecentChange.class)
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofMillis(500)))
                .flatMap(r->recentChangeServiceApi.save(new RecentChangeEntry(r.getUser(),r.getTitle(), Timestamp.valueOf(r.getMeta().getDt().toLocalDateTime()))))
                .onBackpressureLatest()
                .concatMap(e -> recentChangeServiceApi.findMostActive(days), 1)
                .onErrorContinue((t, e) -> {
                });
    }

    public Flux<RecentChange> getRecentChanges() {
        return  webClient.get()
                .uri("https://stream.wikimedia.org/v2/stream/recentchange")
                .retrieve()
                .bodyToFlux(RecentChange.class)
                .filter(Objects::nonNull)
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofMillis(500)))
                .onErrorContinue((t, e) -> {
                    log.warn("OK error {}",t.getClass());
                });
    }

    public Flux<RecentChange> getRecentChanges(String user) {
        return  webClient.get()
                .uri("https://stream.wikimedia.org/v2/stream/recentchange")
                .retrieve()
                .bodyToFlux(RecentChange.class)
                .filter(Objects::nonNull)
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofMillis(500)))
                .filter(r -> Objects.equals(r.getUser(), user))
                .onErrorContinue((t, e) -> {
                    log.warn("OK error {}",t.getClass());
                });
    }
}
