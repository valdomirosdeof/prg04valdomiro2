package br.com.ifba.perfildeusuario.repository;

import br.com.ifba.perfildeusuario.entity.PerfilDeUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilDeUsuarioRepository extends JpaRepository<PerfilDeUsuario, Long> {
    Optional<PerfilDeUsuario> findByNivelDeAcesso(String nivelDeAcesso);
    List<PerfilDeUsuario> findByUsuarioId(Long usuarioId);
    List<PerfilDeUsuario> findByNivelDeAcessoAndUsuarioId(String nivelDeAcesso, Long usuarioId);
}