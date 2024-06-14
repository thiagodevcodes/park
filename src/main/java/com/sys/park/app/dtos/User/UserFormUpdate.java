package com.sys.park.app.dtos.User;

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
public class UserFormUpdate {
    @Size(max = 255, message = "O nome pode ter no máximo 255 caracteres")
    private String name;

    @Size(max = 255, message = "O email pode ter no máximo 255 caracteres")
    private String email;

    @Size(max = 255, message = "O telefone pode ter no máximo 255 caracteres")
    private String phone;

    @Size(max = 255, message = "O nome de usuário pode ter no máximo 255 caracteres")
    private String username;
    
    private Boolean isActive; 
    private String password;
    private String confirmPassword;
}
