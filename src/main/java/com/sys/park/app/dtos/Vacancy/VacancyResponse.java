package com.sys.park.app.dtos.Vacancy;

import java.util.List;

import lombok.Data;

@Data
public class VacancyResponse {
    private Integer vacanciesNotOccupied;
    private Integer vacanciesOccupied;
    private List<VacancyDto> vacanciesList;
}
