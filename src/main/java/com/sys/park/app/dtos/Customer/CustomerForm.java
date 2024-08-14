package com.sys.park.app.dtos.Customer;


import com.sys.park.app.dtos.Person.PersonForm;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

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
public class CustomerForm {
    private Long id;
 
    @Valid
    @NotNull(message = "A pessoa não pode ser nula")
    private PersonForm person;

    @NotNull(message = "O tipo de cliente não pode ser nulo")
    private Long idCustomerType;
    
    @Max(value = 31, message = "O dia do pagamento tem que ser no máximo é 31")
    @Min(value = 1, message = "O dia do pagamento tem que ser no mínimo é 01")
    private Integer paymentDay;

    private Boolean isActive;
}
