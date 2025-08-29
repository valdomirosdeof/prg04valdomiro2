package br.com.ifba.cliente.controller;

import br.com.ifba.cliente.dto.ClienteDeleteResponseDto;
import br.com.ifba.cliente.dto.ClienteGetResponseDto;
import br.com.ifba.cliente.dto.ClientePostRequestDto;
import br.com.ifba.cliente.dto.ClientePutRequestDto;
import br.com.ifba.cliente.entity.Cliente;
import br.com.ifba.cliente.service.ClienteService;
import br.com.ifba.infrastructure.mapper.ObjectMapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/clientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;
    private final ObjectMapperUtil objectMapperUtil;

    @GetMapping(path = "/findall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(objectMapperUtil.mapAll(this.clienteService.findAll(), ClienteGetResponseDto.class));
    }

    @GetMapping(path = "/findbyid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Cliente cliente = this.clienteService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(objectMapperUtil.map(cliente, ClienteGetResponseDto.class));
    }

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody @Valid ClientePostRequestDto clientePostRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(objectMapperUtil.map(clienteService.save((objectMapperUtil.map(clientePostRequestDto, Cliente.class))), ClienteGetResponseDto.class));
    }

    @DeleteMapping(path = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        clienteService.delete(id);
        ClienteDeleteResponseDto response = new ClienteDeleteResponseDto(id, "Cliente deletado com sucesso");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody @Valid ClientePutRequestDto clientePutRequestDto) {
        Cliente cliente = objectMapperUtil.map(clientePutRequestDto, Cliente.class);
        clienteService.update(cliente);
        return ResponseEntity.status(HttpStatus.OK)
                .body(objectMapperUtil.map(cliente, ClienteGetResponseDto.class));
    }
}