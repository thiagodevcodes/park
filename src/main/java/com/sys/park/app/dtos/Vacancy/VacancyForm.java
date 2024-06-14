package com.sys.park.app.dtos.Vacancy;

import jakarta.validation.constraints.NotNull;
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
public class VacancyForm {
    @NotNull(message = "A situação da vaga não pode ser nula")
    private Boolean situation;
}
