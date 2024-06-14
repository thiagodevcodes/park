package com.sys.park.app.dtos.User;

import com.sys.park.app.models.UserRole;

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
public class UserDto {
    private Integer id;
    private String username;
    private String password;
    private Boolean isActive;
    private Integer idPerson;
    private UserRole role;
}
