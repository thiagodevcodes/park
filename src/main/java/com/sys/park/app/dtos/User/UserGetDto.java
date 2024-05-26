package com.sys.park.app.dtos.User;

import lombok.Data;

@Data
public class UserGetDto {
    private Integer id;
    private String username;
    private String name;
    private String phone;
    private String email;
    private Integer userType;
}
