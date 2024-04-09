package com.sys.park.app.dtos.Vacancy;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VacancyForm {
    private Integer id;
    @NotNull(message = "A situação da vaga não pode ser nula")
    private Boolean situation;
}
