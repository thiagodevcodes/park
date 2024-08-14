package com.sys.park.app.dtos.Vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleDto {
    private Long id;
    private String plate;
    private String make;
    private String model;
    private Boolean monthlyVehicle;
}
