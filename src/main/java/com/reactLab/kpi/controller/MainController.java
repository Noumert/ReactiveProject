package com.reactLab.kpi.controller;

import com.reactLab.kpi.entity.TestEntity;
import com.reactLab.kpi.service.TestServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalTime;

@RestController
public class MainController {

    Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    TestServiceApi testServiceApi;

    @GetMapping("/")
    public Mono<String> handler() {
        return Mono.just("Hello world!");
    }

    @GetMapping("/create")
    public Mono<String> create() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("Test");
        return testServiceApi.create(testEntity).map(e-> e.getName()+" id:"+e.getId());
    }

    @GetMapping("/find")
    public Flux<String> findAll() {
        return testServiceApi.findAll().map(e-> e.getName()+" id:"+e.getId() + "\n");
    }

    @GetMapping("/recentChange")
    public Flux<String> recentChange() {
        consumeServerSentEvent();
        return Flux.just("Hello");
    }


    public void consumeServerSentEvent() {
        WebClient client = WebClient.create("https://stream.wikimedia.org/v2/stream");
        ParameterizedTypeReference<ServerSentEvent<String>> type
                = new ParameterizedTypeReference<>() {};

        Flux<ServerSentEvent<String>> eventStream = client.get()
                .uri("/recentchange")
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                content -> logger.info("Time: {} - event: name[{}], id [{}], content[{}] ",
                        LocalTime.now(), content.event(), content.id(), content.data()),
                error -> logger.error("Error receiving SSE: {}", error),
                () -> logger.info("Completed!!!"));
    }
}
