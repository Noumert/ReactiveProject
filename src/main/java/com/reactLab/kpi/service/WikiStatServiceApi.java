package com.reactLab.kpi.service;

import com.reactLab.kpi.dto.MostActiveDto;
import com.reactLab.kpi.dto.MostPopularTitleDto;
import com.reactLab.kpi.dto.RecentChange;
import com.reactLab.kpi.dto.UserInfo;
import reactor.core.publisher.Flux;

import java.util.List;

public interface WikiStatServiceApi {
    Flux<RecentChange> getCurrentChangesStream();

    Flux<RecentChange> getDayChangesStream(int days);

    Flux<UserInfo> getUserStatStream(String user);

    Flux<MostActiveDto> mostActiveUser(Flux<RecentChange> changesDuringDay, int days);

    Flux<List<MostPopularTitleDto>> mostPopularTitles(Flux<RecentChange> recentChangesDayStream);
}
