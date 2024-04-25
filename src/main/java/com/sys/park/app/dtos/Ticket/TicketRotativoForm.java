package com.sys.park.app.dtos.Ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketRotativoForm {
    private Integer id;

    @NotNull(message = "O nome não pode ser nulo!")
    @NotBlank(message = "O nome não pode estar em branco!")
    private String name;

    @NotNull(message = "A placa não pode ser nula!")
    @NotBlank(message = "A placa não pode estar em branco!")
    private String plate;

    @NotNull(message = "A marca do veículo não pode ser nulo!")
    @NotBlank(message = "A marca do veículo não pode estar em branco!")
    private String make;

    @NotNull(message = "O modelo do veículo não pode ser nulo!")
    @NotBlank(message = "O modelo do veículo não pode estar em branco!")
    private String model;

    @NotNull(message = "O nome não pode ser nulo!")
    private Integer idVacancy;

    @NotNull(message = "O nome não pode ser nulo!")
    private Integer idCustomerType;
}
