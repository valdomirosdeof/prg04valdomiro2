package br.com.ifba.usuario.dto;

import br.com.ifba.perfildeusuario.dto.PerfilDeUsuarioGetResponseDto;
import br.com.ifba.pessoa.dto.PessoaGetResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioGetResponseDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty(value = "nome")
    private String nome;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "login")
    private String login;

    @JsonProperty(value = "perfisDeUsuario")
    private List<PerfilDeUsuarioGetResponseDto> perfisDeUsuario;

    @JsonProperty(value = "pessoa")
    private PessoaGetResponseDto pessoa;
}