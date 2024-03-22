package com.sys.park.app.dtos.Vacancy;

import jakarta.validation.constraints.NotNull;

public class VacancyForm {
    @NotNull(message = "A situação da vaga não pode ser nula")
    private Boolean situation;
}
