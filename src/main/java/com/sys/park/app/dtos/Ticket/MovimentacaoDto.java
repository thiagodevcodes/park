package com.sys.park.app.dtos.Ticket;

import java.time.LocalDateTime;

import lombok.Data;

//"Id", "Nome", "Placa", "Marca", "Modelo", "Vaga", "Entrada", "Saída"
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
    private Integer idVehicle;
    private Integer idCustomer;
}
