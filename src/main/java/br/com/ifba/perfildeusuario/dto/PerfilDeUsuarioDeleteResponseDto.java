package br.com.ifba.perfildeusuario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilDeUsuarioDeleteResponseDto {
    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "message")
    private String message = "Perfil de usuário deletado com sucesso";
}