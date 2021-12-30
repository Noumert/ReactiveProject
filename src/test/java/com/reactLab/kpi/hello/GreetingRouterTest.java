package com.reactLab.kpi.hello;

import com.reactLab.kpi.entity.RecentChangeEntry;
import com.reactLab.kpi.repository.RecentChangeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GreetingRouterTest {

    @Autowired
    RecentChangeRepository repository;

    @Test
    public void testHello() {
       RecentChangeEntry recentChangeEntry = new RecentChangeEntry("test","test", Timestamp.valueOf(LocalDateTime.now()));
//        repository
//                .deleteAll()
//                .subscribe(result -> System.out.println(("Entity has been saved: " + result)));
       repository.findAll().subscribe(System.out::println,(error)->{},()-> System.out.println("all good"));
    }

}