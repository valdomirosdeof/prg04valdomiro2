package br.com.ifba.fornecedor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FornecedorDeleteResponseDto {
    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "message")
    private String message = "Fornecedor deletado com sucesso";
}