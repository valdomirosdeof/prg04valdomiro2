package br.com.ifba.usuario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioPutRequestDto {
    //O ID do usuário (id)
    @JsonProperty(value = "id")
    private Long id;

    //O nome do usuário (nome)
    @JsonProperty(value = "nome")
    private String nome;

    //O login do usuario (login)
    @JsonProperty(value = "login")
    private String login;

    //O e-mail do usuário (email)
    @JsonProperty(value = "email")
    private String email;

    //A senha do usuário (senha)
    @JsonProperty(value = "senha")
    private String senha;
}