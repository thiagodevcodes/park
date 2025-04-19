package com.sys.park.app.dtos.Ticket;

import com.sys.park.app.dtos.Customer.CustomerRequest;
import com.sys.park.app.dtos.Vehicle.VehicleRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketRequest {
    private Long id;

    @NotNull(message = "Veiculo não pode ser nulo")
    @Valid
    private VehicleRequest vehicle;

    @NotNull(message = "Cliente não pode ser nulo")
    @Valid
    private CustomerRequest customer;

    @NotNull(message = "A vaga não pode ser nula!")
    private Long idVacancy;
}
