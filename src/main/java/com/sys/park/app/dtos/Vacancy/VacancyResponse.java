package com.sys.park.app.dtos.Vacancy;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VacancyResponse {
    private Integer vacanciesNotOccupied;
    private Integer vacanciesOccupied;
    private List<VacancyDto> vacanciesList;
    private List<VacancyDto> vacanciesNotOccupiedList;
}
