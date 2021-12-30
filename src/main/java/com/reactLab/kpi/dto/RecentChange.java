package com.reactLab.kpi.dto;

import com.reactLab.kpi.dto.Meta;
import lombok.Data;

@Data
public class RecentChange {
    private Long id;
    private String title;
    private String user;
    private Meta meta;
}
