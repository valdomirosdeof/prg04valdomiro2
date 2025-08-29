package br.com.ifba.perfildeusuario.entity;

import br.com.ifba.infrastructure.entity.PersistenceEntity;
import br.com.ifba.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "perfis_usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PerfilDeUsuario extends PersistenceEntity implements Serializable {
    @Column(name = "nivel_acesso", nullable = false)
    private String nivelDeAcesso;

    @Column(name = "permissoes", nullable = false)
    private String permissoes;

    @Column(name = "descricao_perfil")
    private String descricaoDoPerfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;
}