package com.sys.park.app.dtos.User;

import com.sys.park.app.dtos.Person.PersonRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    @NotBlank(message = "O usuário não deve estar em branco")
    private String username;

    @NotNull(message = "A grupo de usuário não deve estar em branco")
    private Long role;

    @Valid
    @NotNull(message = "A pessoa não pode ser nula")
    private PersonRequest person;
}
