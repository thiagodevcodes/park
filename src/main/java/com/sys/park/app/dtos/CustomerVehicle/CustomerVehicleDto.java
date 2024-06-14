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
    private Integer id;
    private Integer idVehicle;
    private Integer idCustomer;
}
