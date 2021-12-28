package com.reactLab.kpi.configs;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalTime;
import java.util.logging.ConsoleHandler;


@Log4j2 // <1>
@Component
class SampleDataInitializer
        implements ApplicationListener<ApplicationReadyEvent> {

    private final ApplicationEventPublisher publisher;

    public SampleDataInitializer(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        WebClient client = WebClient.create("https://stream.wikimedia.org/v2/stream");
        ParameterizedTypeReference<ServerSentEvent<RecentChange>> type
                = new ParameterizedTypeReference<>() {
        };

        Flux<ServerSentEvent<RecentChange>> eventStream = client.get()
                .uri("/recentchange")
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                content -> {
                    RecentChange recentChange = content.data();
                    if(recentChange != null) {
                        publisher.publishEvent(new RecentChangesEvent(recentChange));
                    }
                    log.info("Time: {} - event: name[{}], id [{}], content[{}] ",
                            LocalTime.now(), content.event(), content.id(), content.data());
                },
                error -> log.error("Error receiving SSE: {}", error),
                () -> {
                    log.info("Completed!!!");
                });
    }
}

