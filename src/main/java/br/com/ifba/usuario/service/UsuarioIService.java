package br.com.ifba.usuario.service;

import br.com.ifba.usuario.entity.Usuario;

import java.util.List;

public interface UsuarioIService {
    public abstract Usuario save(Usuario usuario);
    public abstract Usuario update(Usuario usuario);
    public abstract void delete(Usuario usuario);
    public abstract List<Usuario> findAll();
    public abstract Usuario findById(Integer id);
}