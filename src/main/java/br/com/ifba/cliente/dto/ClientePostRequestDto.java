package br.com.ifba.cliente.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientePostRequestDto {
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

    @JsonProperty(value = "cpf")
    @NotNull(message = "O CPF é obrigatório!")
    @NotBlank(message = "O CPF não pode ser vazio!")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 caracteres!")
    private String cpf;

    @JsonProperty(value = "historicoDeCompras")
    private String historicoDeCompras;

    @JsonProperty(value = "limiteDeCredito")
    @DecimalMin(value = "0.0", message = "O limite de crédito deve ser maior ou igual a zero!")
    private BigDecimal limiteDeCredito;
}