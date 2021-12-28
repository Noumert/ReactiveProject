package com.reactLab.kpi.configs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
class WebSocketConfiguration {

    Logger logger = LoggerFactory.getLogger(WebSocketConfiguration.class);

    // <1>
    @Bean
    Executor executor() {
        return Executors.newSingleThreadExecutor();
    }

    // <2>
    @Bean
    HandlerMapping handlerMapping(WebSocketHandler wsh) {
        return new SimpleUrlHandlerMapping() {
            {
                // <3>
                setUrlMap(Map.of("/ws/wikimedia", wsh));
                setOrder(10);
            }
        };
    }

    // <4>
    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    WebSocketHandler webSocketHandler(
            ObjectMapper objectMapper, // <5>
            RecentChangesEventPublisher eventPublisher // <6>
    ) {

        Flux<RecentChangesEvent> publish = Flux.create(eventPublisher).share(); // <7>

        return session -> {

            Flux<WebSocketMessage> messageFlux = publish.map(evt -> {
                try {
                    RecentChange recentChange = (RecentChange) evt.getSource(); // <1>
                    return objectMapper.writeValueAsString(recentChange); // <3>
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).map(str -> {
                logger.info("sending " + str);
                return session.textMessage(str);
            });
//                    .replay(30);

            return session.send(messageFlux);
        };
    }
}