package br.com.ifba.usuario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioPostRequestDto {
    //O nome do usuário (nome)
    @JsonProperty(value = "nome")
    @NotNull(message = "O nome é obrigatório!")
    @NotBlank(message = "O nome não pode ser vazio!")
    private String nome;

    //O login do usuario (login)
    @JsonProperty(value = "login")
    @NotNull(message = "O login é obrigatório!")
    @NotBlank(message = "O login não pode ser vazio!")
    @Size(min = 3, max = 30, message = "O login deve ter entre 3 e 30 caracteres!")
    private String login;

    //O e-mail do usuário (email)
    @JsonProperty(value = "email")
    @NotNull(message = "O e-mail é obrigatório!")
    @NotBlank(message = "O e-mail não pode ser vazio!")
    @Email(message = "E-mail inválido!")
    private String email;

    //A senha do usuário (senha)
    @JsonProperty(value = "senha")
    @NotNull(message = "A senha é obrigatória!")
    @NotBlank(message = "A senha não pode ser vazia!")
    @Size(min = 6, max = 18, message = "A senha deve ter entre 6 e 18 caracteres!")
    private String senha;
}
