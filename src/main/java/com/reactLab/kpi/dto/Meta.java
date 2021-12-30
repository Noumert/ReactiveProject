package com.reactLab.kpi.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Meta {
    private String id;
    private ZonedDateTime dt;
    private String uri;
}
