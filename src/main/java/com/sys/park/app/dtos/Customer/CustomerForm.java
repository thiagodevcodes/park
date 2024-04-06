package com.sys.park.app.dtos.Customer;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerForm {
    @NotBlank(message = "O nome não pode estar em branco")
    @NotEmpty(message = "O nome não pode estar vazio")
    @NotNull(message = "O nome não pode ser nulo")
    private String name;

    @NotBlank(message = "O CPF não pode estar em branco")
    @NotEmpty(message = "O CPF não pode estar vazio")
    @NotNull(message = "O CPF não pode ser nulo")
    @CPF(message = "O CPF informado é inválido")
    private String cpf;

    @NotBlank(message = "O email não pode estar em branco")
    @NotNull(message = "O email não pode ser nulo")
    @Email(message = "O email informado é inválido")
    private String email;

    @NotBlank(message = "O telefone não pode estar em branco")
    @NotNull(message = "O telefone não pode ser nulo")
    @NotEmpty(message = "O telefone não pode estar vazio")
    private String phone;

    @NotNull(message = "O dia do pagamento não pode ser nulo")
    @Max(value = 31, message = "O dia do pagamento tem que ser no máximo é 31")
    @Min(value = 1, message = "O dia do pagamento tem que ser no mínimo é 01")
    private Integer paymentDay;
}
