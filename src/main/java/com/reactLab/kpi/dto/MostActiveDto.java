package com.reactLab.kpi.dto;

import lombok.Data;

@Data
public class MostActiveDto {
    private String user1;
    private int changes;

    public MostActiveDto(String user1, int changes) {
        this.user1 = user1;
        this.changes = changes;
    }
}
