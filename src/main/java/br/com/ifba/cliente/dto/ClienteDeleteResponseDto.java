package br.com.ifba.cliente.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDeleteResponseDto {
    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "message")
    private String message = "Cliente deletado com sucesso";
}