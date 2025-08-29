package br.com.ifba.fornecedor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FornecedorGetResponseDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty(value = "nome")
    private String nome;
    @JsonProperty(value = "telefone")
    private String telefone;
    @JsonProperty(value = "endereco")
    private String endereco;
    @JsonProperty(value = "cnpj")
    private String cnpj;
    @JsonProperty(value = "listaDeProdutos")
    private String listaDeProdutos;
    @JsonProperty(value = "prazoDeEntrega")
    private Integer prazoDeEntrega;
}