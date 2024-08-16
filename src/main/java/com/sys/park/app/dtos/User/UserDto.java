package com.sys.park.app.dtos.User;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDto(
    @NotBlank
    String name,

    @NotBlank
    String username,

    @NotBlank
    String password,

    @CPF
    String cpf,

    @NotBlank
    String phone,

    @Email
    String email,

    @NotNull
    Long role
) {}
