package br.com.ifba.cliente.service;

import br.com.ifba.cliente.entity.Cliente;

import java.util.List;

public interface ClienteIService {
    List<Cliente> findAll();
    Cliente findById(Long id);
    Cliente save(Cliente cliente);
    Cliente delete(Long id);
    Cliente update(Cliente cliente);
}