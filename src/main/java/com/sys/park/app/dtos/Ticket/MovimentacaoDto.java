package com.sys.park.app.dtos.Ticket;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MovimentacaoDto {
    private Integer id;
    private String name;
    private String plate;
    private String make;
    private String model;
    private Integer vacancy;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Integer idCustomerType;
}
