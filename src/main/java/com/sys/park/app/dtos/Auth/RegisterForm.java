package com.sys.park.app.dtos.Auth;

import org.hibernate.validator.constraints.br.CPF;

import com.sys.park.app.models.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterForm {
    @NotBlank(message = "O username não pode estar em branco")
    @NotEmpty(message = "O username não pode estar vazio")
    @NotNull(message = "O username não pode ser nulo")
    String username;

    @NotBlank(message = "O username não pode estar em branco")
    @NotEmpty(message = "O username não pode estar vazio")
    @NotNull(message = "O username não pode ser nulo")
    String name;

    @NotBlank(message = "O cpf não pode estar em branco")
    @NotEmpty(message = "O cpf não pode estar vazio")
    @NotNull(message = "O cpf não pode ser nulo")
    @CPF(message = "CPF Inválido")
    String cpf;

    @NotBlank(message = "O email não pode estar em branco")
    @NotEmpty(message = "O email não pode estar vazio")
    @NotNull(message = "O email não pode ser nulo")
    @Email(message = "O email é inválido")
    String email;

    @NotBlank(message = "O phone não pode estar em branco")
    @NotEmpty(message = "O phone não pode estar vazio")
    @NotNull(message = "O phone não pode ser nulo")
    String phone;

    @NotBlank(message = "A password não pode estar em branco")
    @NotEmpty(message = "A password não pode estar vazio")
    @NotNull(message = "A password não pode ser nulo")
    String password;

    @NotNull(message = "A role não pode ser nulo")
    UserRole role;
}
