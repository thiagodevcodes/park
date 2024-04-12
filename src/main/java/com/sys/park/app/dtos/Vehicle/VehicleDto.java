package com.sys.park.app.dtos.Vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto {
    private Integer id;
    private String plate;
    private String make;
    private String model;
    private Integer idCustomer;
    private Boolean monthlyVehicle;
}
