package com.sys.park.app.dtos.Vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VehicleForm {
    @NotEmpty(message = "A placa não pode ser vazia")
    @NotBlank(message = "A placa não pode estar em branco")
    @Size(max = 10, message = "A placa pode ter no máximo 10 caracteres")
    @NotNull(message = "A placa não pode ser nula")
    private String plate;

    @NotEmpty(message = "A marca não pode ser vazia")
    @Size(max = 100, message = "O marca pode ter no máximo 100 caracteres")
    private String make;

    @NotEmpty(message = "O modelo não pode estar vazio")
    @NotNull(message = "O modelo não pode ser nulo")
    @NotBlank(message = "O modelo não pode estar em branco")
    @Size(message = "O tamanho não pode ultrapassar 20 caracteres")
    private String model;

    @NotEmpty(message = "A marca não pode ser vazia")
    @Size(max = 100, message = "O marca pode ter no máximo 100 caracteres")
    private String type;

    @NotEmpty(message = "A cor não pode ser vazia")
    @Size(max = 100, message = "A cor pode ter no máximo 100 caracteres")
    private String color;
}
