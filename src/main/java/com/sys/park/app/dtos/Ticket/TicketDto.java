package com.sys.park.app.dtos.Ticket;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TicketDto {
    private Integer id;
    private Integer idVehicle;
    private Integer idVacancy;
    private Integer idCustomer;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Double totalPrice;
}
