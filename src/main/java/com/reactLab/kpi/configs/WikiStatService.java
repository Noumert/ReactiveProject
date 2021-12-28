package com.reactLab.kpi.configs;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WikiStatService {
    private final ApplicationEventPublisher publisher; // <1>


    WikiStatService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public Flux<RecentChange> all() { // <3>
        WebClient client = WebClient.create("https://stream.wikimedia.org/v2/stream");
        ParameterizedTypeReference<ServerSentEvent<String>> type
                = new ParameterizedTypeReference<>() {
        };

        Flux<ServerSentEvent<String>> eventStream = client.get()
                .uri("/recentchange")
                .retrieve()
                .bodyToFlux(type);

//        return eventStream;
        return null;
    }
//
//    public Mono<Profile> create(String email) { // <7>
//        return this.profileRepository
//                .save(new Profile(null, email))
//                .doOnSuccess(profile -> this.publisher.publishEvent(new ProfileCreatedEvent(profile)));
//    }
}
