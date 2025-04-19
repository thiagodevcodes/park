package com.sys.park.app.dtos.Ticket;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketMensalRequest {
    private Long id;

    @NotNull(message = "A vaga não pode ser nula!")
    private Long idVacancy;

    @NotNull(message = "O tipo de cliente não pode ser nulo!")
    private Long idCustomerType;

    @NotNull(message = "O veículo não pode ser nulo!")
    private Long idVehicle;

    @NotNull(message = "O cliente não pode ser nulo!")
    private Long idCustomer;
}
