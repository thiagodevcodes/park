package com.sys.park.app.dtos.Person;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PersonMensalista {
    @NotEmpty(message = "O nome não pode ser vazio")
    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres")
    @NotNull(message = "O nome não pode ser nulo")
    private String name;

    @NotEmpty(message = "O cpf não pode ser vazio")
    @NotBlank(message = "O cpf não pode estar em branco")
    @Size(max = 11, message = "O cpf pode ter no máximo 11 caracteres")
    @CPF(message = "CPF Inválido")
    private String cpf;

    @NotEmpty(message = "O email não pode ser vazio")
    @NotBlank(message = "O email não pode estar em branco")
    @Size(max = 100, message = "O email pode ter no máximo 100 caracteres")
    @Email(message = "O email é inválido")
    private String email;

    @NotEmpty(message = "O telefone não pode ser vazio")
    @NotNull(message = "O telefone não pode ser nulo")
    @NotBlank(message = "O telefone não pode estar em branco")
    @Size(max = 30, message = "O telefone pode ter no máximo 30 caracteres")
    private String phone;

    private Integer clientType;
}
