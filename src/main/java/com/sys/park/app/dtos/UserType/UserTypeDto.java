package com.sys.park.app.dtos.UserType;

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
public class UserTypeDto {
    private Integer id;
    private String name;
    private Boolean isActive;
}
