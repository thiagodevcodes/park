package com.sys.park.app.dtos.Vacancy;

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
public class VacancyDto {
    private Long id;
    private Boolean situation;
}
