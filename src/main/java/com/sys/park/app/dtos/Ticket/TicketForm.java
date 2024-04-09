package com.sys.park.app.dtos.Ticket;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TicketForm {
    private Integer id;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Double totalPrice;
}
