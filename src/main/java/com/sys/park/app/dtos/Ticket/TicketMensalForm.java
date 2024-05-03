package com.sys.park.app.dtos.Ticket;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketMensalForm {
    private Integer id;

    @NotNull(message = "A vaga não pode ser nula!")
    private Integer idVacancy;

    @NotNull(message = "O tipo de cliente não pode ser nulo!")
    private Integer idCustomerType;

    @NotNull(message = "O veículo não pode ser nulo!")
    private Integer idVehicle;

    @NotNull(message = "O cliente não pode ser nulo!")
    private Integer idCustomer;
}
