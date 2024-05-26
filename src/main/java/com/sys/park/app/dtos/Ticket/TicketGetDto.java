package com.sys.park.app.dtos.Ticket;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TicketGetDto {
    private Integer id;
    private String name;
    private String plate;
    private String make;
    private String model;
    private Integer idVacancy;
    private LocalDateTime entryTime;
    private Integer idCustomerType;
}
