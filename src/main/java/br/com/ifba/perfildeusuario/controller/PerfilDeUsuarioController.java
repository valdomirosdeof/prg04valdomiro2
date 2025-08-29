package br.com.ifba.perfildeusuario.controller;

import br.com.ifba.infrastructure.mapper.ObjectMapperUtil;
import br.com.ifba.perfildeusuario.dto.PerfilDeUsuarioDeleteResponseDto;
import br.com.ifba.perfildeusuario.dto.PerfilDeUsuarioGetResponseDto;
import br.com.ifba.perfildeusuario.dto.PerfilDeUsuarioPostRequestDto;
import br.com.ifba.perfildeusuario.dto.PerfilDeUsuarioPutRequestDto;
import br.com.ifba.perfildeusuario.entity.PerfilDeUsuario;
import br.com.ifba.perfildeusuario.service.PerfilDeUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/perfis-de-usuario")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PerfilDeUsuarioController {
    private final PerfilDeUsuarioService perfilDeUsuarioService;
    private final ObjectMapperUtil objectMapperUtil;

    @GetMapping(path = "/findall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(objectMapperUtil.mapAll(this.perfilDeUsuarioService.findAll(), PerfilDeUsuarioGetResponseDto.class));
    }

    @GetMapping(path = "/findbyid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        PerfilDeUsuario perfilDeUsuario = this.perfilDeUsuarioService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(objectMapperUtil.map(perfilDeUsuario, PerfilDeUsuarioGetResponseDto.class));
    }

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody @Valid PerfilDeUsuarioPostRequestDto perfilDeUsuarioPostRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(objectMapperUtil.map(perfilDeUsuarioService.save((objectMapperUtil.map(perfilDeUsuarioPostRequestDto, PerfilDeUsuario.class))), PerfilDeUsuarioGetResponseDto.class));
    }

    @DeleteMapping(path = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        perfilDeUsuarioService.delete(id);
        PerfilDeUsuarioDeleteResponseDto response = new PerfilDeUsuarioDeleteResponseDto(id, "Perfil de usuário deletado com sucesso");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody @Valid PerfilDeUsuarioPutRequestDto perfilDeUsuarioPutRequestDto) {
        PerfilDeUsuario perfilDeUsuario = objectMapperUtil.map(perfilDeUsuarioPutRequestDto, PerfilDeUsuario.class);
        perfilDeUsuarioService.update(perfilDeUsuario);
        return ResponseEntity.status(HttpStatus.OK)
                .body(objectMapperUtil.map(perfilDeUsuario, PerfilDeUsuarioGetResponseDto.class));
    }
}