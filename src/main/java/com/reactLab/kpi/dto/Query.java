package com.reactLab.kpi.dto;

import lombok.Data;

import java.util.List;

@Data
public class Query {
    private List<UserContribution> usercontribs;
}
