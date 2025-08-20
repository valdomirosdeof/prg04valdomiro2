package br.com.ifba.usuario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDeleteResponseDto {
    //O ID do usuário deletado (id)
    @JsonProperty(value = "id")
    private Long id;

    //Mensagem de confirmação
    @JsonProperty(value = "message")
    private String message = "Usuário deletado com sucesso";
}