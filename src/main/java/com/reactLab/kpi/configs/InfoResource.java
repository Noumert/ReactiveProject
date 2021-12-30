package com.reactLab.kpi.configs;

import com.reactLab.kpi.dto.Query;
import com.reactLab.kpi.dto.RecentChange;
import com.reactLab.kpi.dto.UserContribution;
import com.reactLab.kpi.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.publisher.Sinks;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/info")
public class InfoResource {
    @Autowired
    private WikiStatService wikiStatService;

    @GetMapping(value = "/{user}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<?> userStat(@PathVariable String user) {
        return wikiStatService.getUserStatStream(user)
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
        return wikiStatService.getRecentChanges().replay(30).autoConnect(0);
    }

    @GetMapping(value = "/recent/{user}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<?> recentUserStream(@PathVariable String user) {
        return wikiStatService.getRecentChanges(user).replay(30).autoConnect(0);
    }

    @GetMapping(value = "/mostActive/{days}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<?> mostActiveUserStream(@PathVariable int days) {
        return wikiStatService.mostActiveUser(days).replay(30).autoConnect(0);
    }
}
