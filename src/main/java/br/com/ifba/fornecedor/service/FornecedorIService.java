package br.com.ifba.fornecedor.service;

import br.com.ifba.fornecedor.entity.Fornecedor;

import java.util.List;

public interface FornecedorIService {
    List<Fornecedor> findAll();
    Fornecedor findById(Long id);
    Fornecedor save(Fornecedor fornecedor);
    Fornecedor delete(Long id);
    Fornecedor update(Fornecedor fornecedor);
}