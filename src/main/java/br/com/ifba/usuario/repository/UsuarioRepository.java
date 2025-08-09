package br.com.ifba.usuario.repository;

import br.com.ifba.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
//Interface UsuarioRepository. Tem conexão com o banco de dados (Nesse caso, PostgreSQL).
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}