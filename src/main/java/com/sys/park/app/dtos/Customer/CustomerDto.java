package com.sys.park.app.dtos.Customer;

import com.sys.park.app.dtos.Person.PersonDto;

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
    private PersonDto person;
}
