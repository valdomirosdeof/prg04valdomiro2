package br.com.ifba.pessoa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaGetResponseDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty(value = "nome")
    private String nome;
    @JsonProperty(value = "telefone")
    private String telefone;
    @JsonProperty(value = "endereco")
    private String endereco;
}