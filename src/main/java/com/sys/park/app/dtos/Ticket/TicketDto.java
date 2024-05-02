package com.sys.park.app.dtos.Ticket;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TicketDto {
    private Integer id;
    private Integer idCustomerVehicle;
    private Integer idVacancy;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Double totalPrice;
    private Boolean isActive;
}
