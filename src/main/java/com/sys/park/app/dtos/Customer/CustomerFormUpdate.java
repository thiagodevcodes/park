package com.sys.park.app.dtos.Customer;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

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
public class CustomerFormUpdate {
    private Integer id;

    @NotBlank(message = "O nome não pode estar em branco")
    @NotEmpty(message = "O nome não pode estar vazio")
    private String name;

    @NotBlank(message = "O CPF não pode estar em branco")
    @NotEmpty(message = "O CPF não pode estar vazio")
    @CPF(message = "O CPF informado não é válido")
    private String cpf;

    @NotBlank(message = "O email não pode estar em branco")
    @NotEmpty(message = "O email não pode estar vazio")
    @Email(message = "O email informado não é válido")
    private String email;

    @NotBlank(message = "O telefone não pode estar em branco")
    @NotEmpty(message = "O telefone não pode estar vazio")
    private String phone;

    @Max(value = 31, message = "O dia do pagamento tem que ser no máximo é 31")
    @Min(value = 1, message = "O dia do pagamento tem que ser no mínimo é 01")
    private Integer paymentDay;

    private Integer clientType;

    private Boolean isActive;
}
