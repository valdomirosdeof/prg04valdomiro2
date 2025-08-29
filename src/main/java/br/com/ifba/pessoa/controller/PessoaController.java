package br.com.ifba.pessoa.controller;

import br.com.ifba.infrastructure.mapper.ObjectMapperUtil;
import br.com.ifba.pessoa.dto.PessoaDeleteResponseDto;
import br.com.ifba.pessoa.dto.PessoaGetResponseDto;
import br.com.ifba.pessoa.dto.PessoaPostRequestDto;
import br.com.ifba.pessoa.dto.PessoaPutRequestDto;
import br.com.ifba.pessoa.entity.Pessoa;
import br.com.ifba.pessoa.service.PessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/pessoas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PessoaController {
    private final PessoaService pessoaService;
    private final ObjectMapperUtil objectMapperUtil;

    @GetMapping(path = "/findall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(objectMapperUtil.mapAll(this.pessoaService.findAll(), PessoaGetResponseDto.class));
    }

    @GetMapping(path = "/findbyid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Pessoa pessoa = this.pessoaService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(objectMapperUtil.map(pessoa, PessoaGetResponseDto.class));
    }

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody @Valid PessoaPostRequestDto pessoaPostRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(objectMapperUtil.map(pessoaService.save((objectMapperUtil.map(pessoaPostRequestDto, Pessoa.class))), PessoaGetResponseDto.class));
    }

    @DeleteMapping(path = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        pessoaService.delete(id);
        PessoaDeleteResponseDto response = new PessoaDeleteResponseDto(id, "Pessoa deletada com sucesso");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody @Valid PessoaPutRequestDto pessoaPutRequestDto) {
        Pessoa pessoa = objectMapperUtil.map(pessoaPutRequestDto, Pessoa.class);
        pessoaService.update(pessoa);
        return ResponseEntity.status(HttpStatus.OK)
                .body(objectMapperUtil.map(pessoa, PessoaGetResponseDto.class));
    }
}