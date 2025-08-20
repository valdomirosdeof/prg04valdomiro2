package br.com.ifba.usuario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioGetResponseDto {
    //O nome do usuário (nome)
    @JsonProperty(value = "nome")
    private String nome;
    //O e-mail do usuário (email)
    @JsonProperty(value = "email")
    private String email;
}
