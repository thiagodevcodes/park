package com.sys.park.app.dtos.Auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenDto {
    String token;
    Boolean valid;
    String subject;
}
