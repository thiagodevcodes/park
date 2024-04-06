package com.sys.park.app.dtos.Customer;

import lombok.Data;

@Data
public class CustomerMensalista {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String cpf;
    private Integer paymentDay;
    private String clientName;
    private Boolean isActive;
}
