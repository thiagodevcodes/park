package com.sys.park.app.dtos.CustomerVehicle;

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
public class CustomerVehicleDto {
    private Long id;
    private String plate;
    private String make;
    private String model; 
    private Boolean isActive;
    private Long idVehicle;
    private Long idCustomer;
}
