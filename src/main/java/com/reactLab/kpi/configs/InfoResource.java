package com.reactLab.kpi.configs;

import com.reactLab.kpi.dto.*;
import com.reactLab.kpi.service.RecentChangeServiceApi;
import com.reactLab.kpi.service.WikiStatServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/info")
public class InfoResource {

    private final Flux<RecentChange> recentChangesStream;
    private final Flux<RecentChange> recentChangesDayStream;
    private WikiStatServiceApi wikiStatServiceApi;

    @Autowired
    public InfoResource(WikiStatServiceApi wikiStatServiceApi, RecentChangeServiceApi recentChangeServiceApi) {
        recentChangeServiceApi.deleteAll().subscribe();
        this.wikiStatServiceApi = wikiStatServiceApi;

        recentChangesDayStream = wikiStatServiceApi.getDayChangesStream(1);
        recentChangesStream = wikiStatServiceApi.getCurrentChangesStream();
    }

    @GetMapping(value = "/{user}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<?> userStat(@PathVariable String user) {
        return wikiStatServiceApi.getUserStatStream(user)
                .map(UserInfo::getQuery)
                .map(Query::getUsercontribs)
                .map(this::groupContribsByTitle)
                .replay(30).autoConnect(0);

    }

    private List<List<UserContribution>> groupContribsByTitle(List<UserContribution> userContributions) {
        return userContributions.stream()
                .collect(Collectors.groupingBy(UserContribution::getTitle))
                .values()
                .stream()
                .sorted((uc1, uc2) -> Integer.compare(uc2.size(), uc1.size()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/recent", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<?> recentStream() {
        return recentChangesStream
                .replay(30)
                .autoConnect(0);
    }

    @GetMapping(value = "/recent/{user}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<?> recentUserStream(@PathVariable String user) {
        return recentChangesStream
                .filter(recentChange -> recentChange.getUser().equals(user))
                .replay(30)
                .autoConnect(0);
    }

    @GetMapping(value = "/mostActive/{days}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<?> mostActiveUserStream(@PathVariable int days) {
        return wikiStatServiceApi.mostActiveUser(recentChangesDayStream,days).replay(30)
                .autoConnect(0);
    }
}
