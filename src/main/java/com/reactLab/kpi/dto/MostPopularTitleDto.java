package com.reactLab.kpi.dto;

import lombok.Data;

@Data
public class MostPopularTitleDto {
    String title;
    private int changes;

    public MostPopularTitleDto(String title, int changes) {
        this.title = title;
        this.changes = changes;
    }
}
