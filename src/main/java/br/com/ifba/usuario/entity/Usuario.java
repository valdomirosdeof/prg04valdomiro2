package br.com.ifba.usuario.entity;

import br.com.ifba.infrastructure.entity.PersistenceEntity;
import br.com.ifba.perfildeusuario.entity.PerfilDeUsuario;
import br.com.ifba.pessoa.entity.Pessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Usuario extends PersistenceEntity implements Serializable {
    //Atributos (Colunas) da tabela "usuarios".
    //O nome não deve ser nulo.
    @Column(name = "nome", nullable = false)
    private String nome;
    //O login deve ser único e não deve ser nulo.
    @Column(name = "login", nullable = false, unique = true)
    private String login;
    //O e-mail deve ser único e não deve ser nulo.
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    //A senha não deve ser nula.
    @Column(name = "senha", nullable = false)
    private String senha;

    //Relacionamento com PerfilDeUsuario
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PerfilDeUsuario> perfisDeUsuario = new ArrayList<>();

    //Relacionamento com Pessoa
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pessoa_id", referencedColumnName = "id")
    private Pessoa pessoa;
}