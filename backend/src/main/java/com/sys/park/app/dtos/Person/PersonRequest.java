package com.sys.park.app.dtos.Person;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class PersonRequest {
    @NotEmpty(message = "O nome não pode ser vazio")
    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres")
    @NotNull(message = "O nome não pode ser nulo")
    private String name;

    @CPF(message = "CPF Inválido")
    private String cpf;

    @Email(message = "O email é inválido")
    private String email;

    @Size(max = 30, message = "O telefone pode ter no máximo 30 caracteres")
    private String phone;
}
