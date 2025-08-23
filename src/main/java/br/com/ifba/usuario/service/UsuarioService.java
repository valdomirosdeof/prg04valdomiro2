package br.com.ifba.usuario.service;

import br.com.ifba.infrastructure.exception.BusinessException;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//UsuarioService e seus métodos sobrescritos. Tem conexão com a camada Repository e contém as regras do sistema. Implementa os métodos abstratos do UsuarioIService.
@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioIService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado com ID: " + id));
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        // Verifica duplicidade de login.
        if (usuarioRepository.findByLogin(usuario.getLogin()).isPresent()) {
            throw new BusinessException("Login já está em uso por outro usuário: " + usuario.getLogin());
        }

        // Verifica duplicidade de e-mail.
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new BusinessException("-Email já está em uso por outro usuário: " + usuario.getEmail());
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado com ID: " + id));
        usuarioRepository.delete(usuario);
        return usuario;
    }

    @Override
    @Transactional
    public Usuario update(Usuario usuario) {
        if (!usuarioRepository.existsById(usuario.getId())) {
            throw new BusinessException("Usuário não encontrado com ID: " + usuario.getId());
        }

        // Verificar duplicidade de login (exceto o próprio usuário)
        usuarioRepository.findByLogin(usuario.getLogin())
                .filter(u -> !u.getId().equals(usuario.getId()))
                .ifPresent(u -> {
                    throw new BusinessException("Login já está em uso por outro usuário: " + usuario.getLogin());
                });

        // Verificar duplicidade de email (exceto o próprio usuário)
        usuarioRepository.findByEmail(usuario.getEmail())
                .filter(u -> !u.getId().equals(usuario.getId()))
                .ifPresent(u -> {
                    throw new BusinessException("E-mail já está em uso por outro usuário: " + usuario.getEmail());
                });

        return usuarioRepository.save(usuario);
    }
}