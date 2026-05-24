package com.gisellepiercing.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "O primeiro nome é obrigatório")
    private String firstName;

    @NotBlank(message = "O sobrenome é obrigatório")
    private String lastName;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;

    @NotBlank(message = "O CPF é obrigatório")
    @CPF(message = "O CPF informado é inválido")
    private String cpf;
}