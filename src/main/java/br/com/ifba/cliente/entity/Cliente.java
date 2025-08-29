package br.com.ifba.cliente.entity;

import br.com.ifba.pessoa.entity.Pessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Cliente extends Pessoa {
    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "historico_compras")
    private String historicoDeCompras;

    @Column(name = "limite_credito")
    private BigDecimal limiteDeCredito;
}