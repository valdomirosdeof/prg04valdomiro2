package br.com.ifba.usuario.controller;

import br.com.ifba.infrastructure.mapper.ObjectMapperUtil;
import br.com.ifba.usuario.dto.UsuarioDeleteResponseDto;
import br.com.ifba.usuario.dto.UsuarioGetResponseDto;
import br.com.ifba.usuario.dto.UsuarioPostRequestDto;
import br.com.ifba.usuario.dto.UsuarioPutRequestDto;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//UsuarioController e seus métodos de requisição mapeados. Tem conexão com a camada Service e contém as funcionalidades do sistema.
@RestController
@RequestMapping(path = "/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final ObjectMapperUtil objectMapperUtil;
    //Obtém informações do servidor. Nesse caso, encontra todos os usuários cadastrados no sistema.
    @GetMapping(path = "/findall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(objectMapperUtil.mapAll(this.usuarioService.findAll(), UsuarioGetResponseDto.class));
    }

    //Obtém informações do servidor. Nesse caso, encontra um usuário cadastrado no sistema (pelo ID).
    @GetMapping(path = "/findbyid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Usuario usuario = this.usuarioService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(objectMapperUtil.map(usuario, UsuarioGetResponseDto.class));
    }

    //Envia informações ao servidor. Nesse caso, cadastra um usuário no sistema.
    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody UsuarioPostRequestDto usuarioPostRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(objectMapperUtil.map(usuarioService.save((objectMapperUtil.map(usuarioPostRequestDto, Usuario.class))), UsuarioGetResponseDto.class));
    }

    //Remove informações do servidor. Nesse caso, remove um usuário do sistema.
    @DeleteMapping(path = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        UsuarioDeleteResponseDto response = new UsuarioDeleteResponseDto(id, "Usuário deletado com sucesso");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //Atualiza informações no servidor. Nesse caso, atualiza uma ou mais informações de um usuário cadastrado no sistema.
    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody UsuarioPutRequestDto usuarioPutRequestDto) {
        Usuario usuario = objectMapperUtil.map(usuarioPutRequestDto, Usuario.class);
        usuarioService.update(usuario);
        return ResponseEntity.status(HttpStatus.OK)
                .body(objectMapperUtil.map(usuario, UsuarioGetResponseDto.class));
    }
}