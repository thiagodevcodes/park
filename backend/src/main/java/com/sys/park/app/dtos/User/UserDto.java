package com.sys.park.app.dtos.User;


import com.sys.park.app.dtos.Person.PersonDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Long role;
    private PersonDto person;
}
