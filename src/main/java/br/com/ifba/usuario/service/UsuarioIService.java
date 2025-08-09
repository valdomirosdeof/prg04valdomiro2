package br.com.ifba.usuario.service;

import br.com.ifba.usuario.entity.Usuario;

import java.util.List;
//Interface UsuarioIService.
public interface UsuarioIService {
    List<Usuario> findAll();
    Usuario findById(Long id);
    Usuario save(Usuario usuario);
    Usuario delete(Long id);
    Usuario update(Usuario usuario);
}