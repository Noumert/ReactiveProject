package com.reactLab.kpi.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContributionStats {
    private List<UserContribution> userContributions;
    private int count;
}
