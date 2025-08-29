package br.com.ifba.cliente.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteGetResponseDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty(value = "nome")
    private String nome;
    @JsonProperty(value = "telefone")
    private String telefone;
    @JsonProperty(value = "endereco")
    private String endereco;
    @JsonProperty(value = "cpf")
    private String cpf;
    @JsonProperty(value = "historicoDeCompras")
    private String historicoDeCompras;
    @JsonProperty(value = "limiteDeCredito")
    private BigDecimal limiteDeCredito;
}