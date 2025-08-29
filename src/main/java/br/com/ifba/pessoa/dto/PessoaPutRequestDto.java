package br.com.ifba.pessoa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaPutRequestDto {
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
}