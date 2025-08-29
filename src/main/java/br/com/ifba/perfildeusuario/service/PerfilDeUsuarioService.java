package br.com.ifba.perfildeusuario.service;

import br.com.ifba.infrastructure.exception.BusinessException;
import br.com.ifba.perfildeusuario.entity.PerfilDeUsuario;
import br.com.ifba.perfildeusuario.repository.PerfilDeUsuarioRepository;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerfilDeUsuarioService implements PerfilDeUsuarioIService {
    private final PerfilDeUsuarioRepository perfilDeUsuarioRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<PerfilDeUsuario> findAll() {
        return perfilDeUsuarioRepository.findAll();
    }

    @Override
    public PerfilDeUsuario findById(Long id) {
        return perfilDeUsuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Perfil de usuário não encontrado com ID: " + id));
    }

    @Override
    @Transactional
    public PerfilDeUsuario save(PerfilDeUsuario perfilDeUsuario) {
        return perfilDeUsuarioRepository.save(perfilDeUsuario);
    }

    @Override
    @Transactional
    public PerfilDeUsuario delete(Long id) {
        PerfilDeUsuario perfilDeUsuario = perfilDeUsuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Perfil de usuário não encontrado com ID: " + id));
        perfilDeUsuarioRepository.delete(perfilDeUsuario);
        return perfilDeUsuario;
    }

    @Override
    @Transactional
    public PerfilDeUsuario update(PerfilDeUsuario perfilDeUsuario) {
        if (!perfilDeUsuarioRepository.existsById(perfilDeUsuario.getId())) {
            throw new BusinessException("Perfil de usuário não encontrado com ID: " + perfilDeUsuario.getId());
        }
        return perfilDeUsuarioRepository.save(perfilDeUsuario);
    }

    public List<PerfilDeUsuario> findByUsuarioId(Long usuarioId) {
        return perfilDeUsuarioRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public PerfilDeUsuario saveWithUsuario(PerfilDeUsuario perfil, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado com ID: " + usuarioId));

        perfil.setUsuario(usuario);
        return perfilDeUsuarioRepository.save(perfil);
    }
}