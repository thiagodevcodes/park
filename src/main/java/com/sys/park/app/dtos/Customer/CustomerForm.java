package com.sys.park.app.dtos.Customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerForm {
    @NotEmpty(message = "A placa não pode ser vazia")
    @NotBlank(message = "A placa não pode estar em branco")
    @NotNull(message = "A placa não pode ser nula")
    private String idCostumerType;

    @NotEmpty(message = "A placa não pode ser vazia")
    @NotBlank(message = "A placa não pode estar em branco")
    @NotNull(message = "A placa não pode ser nula")
    private String idPerson;
}
