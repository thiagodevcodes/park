package com.sys.park.app.dtos.Person;

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
public class PersonDto {
    private Long id;
    private String name;
    private String cpf;
    private String email;
    private String phone;
}
