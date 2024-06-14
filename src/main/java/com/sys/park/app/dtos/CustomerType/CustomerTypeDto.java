package com.sys.park.app.dtos.CustomerType;

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
public class CustomerTypeDto {
    private Integer id;
    private String name;
    private Boolean isActive;
}
