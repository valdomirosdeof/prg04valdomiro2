package br.com.ifba.perfildeusuario.service;

import br.com.ifba.perfildeusuario.entity.PerfilDeUsuario;

import java.util.List;

public interface PerfilDeUsuarioIService {
    List<PerfilDeUsuario> findAll();
    PerfilDeUsuario findById(Long id);
    PerfilDeUsuario save(PerfilDeUsuario perfilDeUsuario);
    PerfilDeUsuario delete(Long id);
    PerfilDeUsuario update(PerfilDeUsuario perfilDeUsuario);
}