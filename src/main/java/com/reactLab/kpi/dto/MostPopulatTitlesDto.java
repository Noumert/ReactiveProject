package com.reactLab.kpi.dto;

import lombok.Data;

import java.util.List;

@Data
public class MostPopulatTitlesDto {
    List<MostPopularTitleDto> mostPopularTitleDtoList;

    public MostPopulatTitlesDto(List<MostPopularTitleDto> mostPopularTitleDtoList) {
        this.mostPopularTitleDtoList = mostPopularTitleDtoList;
    }
}
