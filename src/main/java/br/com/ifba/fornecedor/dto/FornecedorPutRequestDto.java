package br.com.ifba.fornecedor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FornecedorPutRequestDto {
    @JsonProperty(value = "id")
    @NotNull(message = "O ID é obrigatório!")
    private Long id;

    @JsonProperty(value = "nome")
    @NotNull(message = "O nome é obrigatório!")
    @NotBlank(message = "O nome não pode ser vazio!")
    private String nome;

    @JsonProperty(value = "telefone")
    @NotNull(message = "O telefone é obrigatório!")
    @NotBlank(message = "O telefone não pode ser vazio!")
    private String telefone;

    @JsonProperty(value = "endereco")
    @NotNull(message = "O endereço é obrigatório!")
    @NotBlank(message = "O endereço não pode ser vazio!")
    private String endereco;

    @JsonProperty(value = "cnpj")
    @NotNull(message = "O CNPJ é obrigatório!")
    @NotBlank(message = "O CNPJ não pode ser vazio!")
    @Size(min = 14, max = 14, message = "O CNPJ deve ter 14 caracteres!")
    private String cnpj;

    @JsonProperty(value = "listaDeProdutos")
    private String listaDeProdutos;

    @JsonProperty(value = "prazoDeEntrega")
    @Min(value = 1, message = "O prazo de entrega deve ser maior que zero!")
    private Integer prazoDeEntrega;
}