package br.com.ifba.fornecedor.controller;

import br.com.ifba.fornecedor.dto.FornecedorDeleteResponseDto;
import br.com.ifba.fornecedor.dto.FornecedorGetResponseDto;
import br.com.ifba.fornecedor.dto.FornecedorPostRequestDto;
import br.com.ifba.fornecedor.dto.FornecedorPutRequestDto;
import br.com.ifba.fornecedor.entity.Fornecedor;
import br.com.ifba.fornecedor.service.FornecedorService;
import br.com.ifba.infrastructure.mapper.ObjectMapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/fornecedores")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FornecedorController {
    private final FornecedorService fornecedorService;
    private final ObjectMapperUtil objectMapperUtil;

    @GetMapping(path = "/findall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(objectMapperUtil.mapAll(this.fornecedorService.findAll(), FornecedorGetResponseDto.class));
    }

    @GetMapping(path = "/findbyid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Fornecedor fornecedor = this.fornecedorService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(objectMapperUtil.map(fornecedor, FornecedorGetResponseDto.class));
    }

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody @Valid FornecedorPostRequestDto fornecedorPostRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(objectMapperUtil.map(fornecedorService.save((objectMapperUtil.map(fornecedorPostRequestDto, Fornecedor.class))), FornecedorGetResponseDto.class));
    }

    @DeleteMapping(path = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        fornecedorService.delete(id);
        FornecedorDeleteResponseDto response = new FornecedorDeleteResponseDto(id, "Fornecedor deletado com sucesso");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody @Valid FornecedorPutRequestDto fornecedorPutRequestDto) {
        Fornecedor fornecedor = objectMapperUtil.map(fornecedorPutRequestDto, Fornecedor.class);
        fornecedorService.update(fornecedor);
        return ResponseEntity.status(HttpStatus.OK)
                .body(objectMapperUtil.map(fornecedor, FornecedorGetResponseDto.class));
    }
}