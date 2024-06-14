package com.sys.park.app.dtos.Ticket;

import com.sys.park.app.dtos.Customer.CustomerForm;
import com.sys.park.app.dtos.Vehicle.VehicleForm;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketForm {
    private Integer id;

    @NotNull(message = "Veiculo não pode ser nulo")
    @Valid
    private VehicleForm vehicle;

    @NotNull(message = "Cliente não pode ser nulo")
    @Valid
    private CustomerForm customer;

    @NotNull(message = "A vaga não pode ser nula!")
    private Integer idVacancy;
}
