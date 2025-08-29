package br.com.ifba.perfildeusuario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilDeUsuarioPostRequestDto {
    @JsonProperty(value = "nivelDeAcesso")
    @NotNull(message = "O nível de acesso é obrigatório!")
    @NotBlank(message = "O nível de acesso não pode ser vazio!")
    private String nivelDeAcesso;

    @JsonProperty(value = "permissoes")
    @NotNull(message = "As permissões são obrigatórias!")
    @NotBlank(message = "As permissões não podem ser vazias!")
    private String permissoes;

    @JsonProperty(value = "descricaoDoPerfil")
    private String descricaoDoPerfil;

    @JsonProperty(value = "usuarioId")
    private Long usuarioId;
}