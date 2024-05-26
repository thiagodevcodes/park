package com.sys.park.app.dtos.Vehicle;

import lombok.Data;

@Data
public class VehicleGetDto {
    private Integer id;
    private String plate;
    private String make;
    private String model;
}
