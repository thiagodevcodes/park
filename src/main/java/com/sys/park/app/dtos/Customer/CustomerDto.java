package com.sys.park.app.dtos.Customer;

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
public class CustomerDto {
    private Integer id;
    private Integer idPerson;
    private Integer paymentDay;
    private Integer idCustomerType;
    private Boolean isActive;
}
