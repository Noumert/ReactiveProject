package com.reactLab.kpi.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class UserContribution {
    private Long userid;
    private String user;
    private String title;
    private ZonedDateTime timestamp;
}
