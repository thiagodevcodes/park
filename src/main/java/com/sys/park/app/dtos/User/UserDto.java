package com.sys.park.app.dtos.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String username;
    private String password;
    private Integer userType;
    private Boolean isActive;
    private Integer idPerson;
    private Integer idUserType;
}
