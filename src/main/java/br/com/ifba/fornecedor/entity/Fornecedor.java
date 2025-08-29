package br.com.ifba.fornecedor.entity;

import br.com.ifba.pessoa.entity.Pessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fornecedores")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Fornecedor extends Pessoa {
    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;

    @Column(name = "lista_produtos")
    private String listaDeProdutos;

    @Column(name = "prazo_entrega")
    private Integer prazoDeEntrega;
}