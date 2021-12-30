package com.reactLab.kpi.service;

import com.reactLab.kpi.dto.MostActiveDto;
import com.reactLab.kpi.dto.RecentChange;
import com.reactLab.kpi.dto.UserInfo;
import com.reactLab.kpi.entity.RecentChangeEntry;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;

public interface WikiStatServiceApi {
    Flux<RecentChange> getCurrentChangesStream();

    Flux<RecentChange> getDayChangesStream(int days);

    Flux<UserInfo> getUserStatStream(String user);

    Flux<MostActiveDto> mostActiveUser(Flux<RecentChange> changesDuringDay, int days);
}
