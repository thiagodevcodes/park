package com.sys.park.app.dtos.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserForm {
    @NotEmpty(message = "O login não pode ser vazio")
    @NotBlank(message = "O login não pode estar em branco")
    @Size(max = 255, message = "O login pode ter no máximo 255 caracteres")
    @NotNull(message = "O login não pode ser nulo")
    private String login;

    @NotEmpty(message = "O password não pode ser vazio")
    @NotNull(message = "O password não pode ser nulo")
    @NotBlank(message = "O password não pode estar em branco")
    @Size(max = 255, message = "O password pode ter no máximo 255 caracteres")
    private String password;
}
