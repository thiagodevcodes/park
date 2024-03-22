package com.sys.park.app.dtos.Ticket;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TicketForm {
    @NotNull(message = "O veiculo não pode ser nulo")
    private Integer idVehicle;

    @NotNull(message = "A vaga não pode ser nula")
    private Integer idVacancy;

    @NotNull(message = "O login não pode ser nulo")
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private Float totalPrice;
}
