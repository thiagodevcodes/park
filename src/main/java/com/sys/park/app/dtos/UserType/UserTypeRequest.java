package com.sys.park.app.dtos.UserType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class UserTypeRequest {
    @NotEmpty(message = "O nome não pode ser vazia")
    @NotBlank(message = "O nome não pode estar em branco")
    @NotNull(message = "O nome não pode ser nula")
    private String name;
}
