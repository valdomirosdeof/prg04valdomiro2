package br.com.ifba.pessoa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaDeleteResponseDto {
    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "message")
    private String message = "Pessoa deletada com sucesso";
}