package com.sys.park.app.dtos.Ticket;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TicketForm {
    private Integer id;
    private Integer idVehicle;
    private Integer idVacancy;
    private Integer idCustomer;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Boolean isActive;
    private Double totalPrice;
}
