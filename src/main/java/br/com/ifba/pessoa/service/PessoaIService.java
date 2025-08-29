package br.com.ifba.pessoa.service;

import br.com.ifba.pessoa.entity.Pessoa;

import java.util.List;

public interface PessoaIService {
    List<Pessoa> findAll();
    Pessoa findById(Long id);
    Pessoa save(Pessoa pessoa);
    Pessoa delete(Long id);
    Pessoa update(Pessoa pessoa);
}