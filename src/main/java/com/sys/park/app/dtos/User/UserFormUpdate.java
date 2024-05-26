package com.sys.park.app.dtos.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserFormUpdate {
    @NotNull(message = "O tipo de usuário não pode ser nulo")
    private Integer userType;
 
    @NotEmpty(message = "O nome não pode ser vazio")
    @NotNull(message = "O nome não pode ser nulo")
    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 255, message = "O nome pode ter no máximo 255 caracteres")
    private String name;

    @NotEmpty(message = "O email não pode ser vazio")
    @NotNull(message = "O email não pode ser nulo")
    @NotBlank(message = "O email não pode estar em branco")
    @Size(max = 255, message = "O email pode ter no máximo 255 caracteres")
    private String email;

    @NotEmpty(message = "O telefone não pode ser vazio")
    @NotNull(message = "O telefone não pode ser nulo")
    @NotBlank(message = "O telefone não pode estar em branco")
    @Size(max = 255, message = "O telefone pode ter no máximo 255 caracteres")
    private String phone;

    @NotEmpty(message = "O nome de usuário não pode ser vazio")
    @NotBlank(message = "O nome de usuário não pode estar em branco")
    @Size(max = 255, message = "O nome de usuário pode ter no máximo 255 caracteres")
    @NotNull(message = "O nome de usuário não pode ser nulo")
    private String username;
    
    private String password;
    private String confirmPassword;
}
