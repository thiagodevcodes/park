package com.sys.park.app.dtos.Customer;

import lombok.Data;

@Data
public class CustomerDto {
    private Integer id;
    private Integer idPerson;
    private Integer paymentDay;
    private Integer idCustomerType;
    private Boolean isActive;
}
