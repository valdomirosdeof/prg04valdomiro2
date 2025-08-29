package br.com.ifba.perfildeusuario.dto;

import br.com.ifba.usuario.dto.UsuarioGetResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilDeUsuarioGetResponseDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty(value = "nivelDeAcesso")
    private String nivelDeAcesso;
    @JsonProperty(value = "permissoes")
    private String permissoes;
    @JsonProperty(value = "descricaoDoPerfil")
    private String descricaoDoPerfil;

    @JsonProperty(value = "usuario")
    private UsuarioGetResponseDto usuario;
}