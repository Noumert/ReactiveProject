package com.reactLab.kpi.dto;

import lombok.Data;

@Data
public class MostActiveDto {
    private String user;
    private int changes;

    public MostActiveDto(String user, int changes) {
        this.user = user;
        this.changes = changes;
    }
}
