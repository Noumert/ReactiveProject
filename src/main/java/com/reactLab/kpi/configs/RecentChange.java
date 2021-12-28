package com.reactLab.kpi.configs;

import lombok.Data;

@Data
public class RecentChange {
    private Long id;
    private String title;
    private String user;
    private Meta meta;
}
