package br.com.ifba.usuario.service;

import br.com.ifba.infrastructure.exception.BusinessException;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//UsuarioService e seus métodos sobrescritos. Tem conexão com a camada Repository e contém as regras do sistema. Implementa os métodos abstratos do UsuarioIService.
@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioIService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            throw new BusinessException("Nenhum usuário encontrado!");
        }
        return usuarios;
    }

    @Override
    public Usuario findById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException("ID do usuário deve ser um valor válido e maior que zero!");
        }
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado com ID: " + id));
    }

    @Override
    public Usuario save(Usuario usuario) {
        if (usuario == null) {
            throw new BusinessException("Dados do usuário não podem ser nulos!");//Não funciona.
        }

        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new BusinessException("Nome do usuário é obrigatório!");
        }

        if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty()) {
            throw new BusinessException("Login do usuário é obrigatório!");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new BusinessException("E-mail do usuário é obrigatório!");
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new BusinessException("Senha do usuário é obrigatória!");
        }

        //Verifica se o login já pertence a outro usuário.
        usuarioRepository.findByLogin(usuario.getLogin())
                .ifPresent(u -> {
                    throw new BusinessException("Login '" + usuario.getLogin() + "' já está em uso!");
                });

        //Verifica se o e-mail já pertence a outro usuário.
        usuarioRepository.findByEmail(usuario.getEmail())
                .ifPresent(u -> {
                    throw new BusinessException("E-mail '" + usuario.getEmail() + "' já está em uso!");
                });

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario delete(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException("ID do usuário deve ser um valor válido e maior que zero!");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado com ID: " + id));

        usuarioRepository.delete(usuario);
        return usuario;
    }

    @Override
    public Usuario update(Usuario usuario) {
        if (usuario == null) {
            throw new BusinessException("Dados do usuário não podem ser nulos!");//Não funciona.
        }

        if (usuario.getId() == null || usuario.getId() <= 0) {
            throw new BusinessException("ID do usuário deve ser um valor válido e maior que zero para atualização!");
        }

        //Verifica se o usuário existe antes de atualizar.
        Usuario usuarioExistente = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado com ID: " + usuario.getId()));

        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new BusinessException("Nome do usuário é obrigatório!");
        }

        if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty()) {
            throw new BusinessException("Login do usuário é obrigatório!");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new BusinessException("E-mail do usuário é obrigatório!");
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new BusinessException("Senha do usuário é obrigatória!");
        }

        //Verifica se o login já pertence a outro usuário.
        usuarioRepository.findByLogin(usuario.getLogin())
                .filter(u -> !u.getId().equals(usuario.getId()))
                .ifPresent(u -> {
                    throw new BusinessException("Login '" + usuario.getLogin() + "' já está em uso por outro usuário!");
                });

        //Verifica se o e-mail já pertence a outro usuário.
        usuarioRepository.findByEmail(usuario.getEmail())
                .filter(u -> !u.getId().equals(usuario.getId()))
                .ifPresent(u -> {
                    throw new BusinessException("E-mail '" + usuario.getEmail() + "' já está em uso por outro usuário!");
                });

        return usuarioRepository.save(usuario);
    }
}