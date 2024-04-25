package com.sys.park.app.dtos.Ticket;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketMensalForm {
    private Integer id;

    @NotNull(message = "O id da Vaga não pode ser nulo!")
    private Integer idVacancy;

    @NotNull(message = "O tipo de Cliente não pode ser nulo!")
    private Integer idCustomerType;

    @NotNull(message = "O id do Veículo não pode ser nulo!")
    private Integer idVehicle;

    @NotNull(message = "O id do Cliente não pode ser nulo!")
    private Integer idCustomer;
}
