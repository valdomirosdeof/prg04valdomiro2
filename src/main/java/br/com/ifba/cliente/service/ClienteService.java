package br.com.ifba.cliente.service;

import br.com.ifba.cliente.entity.Cliente;
import br.com.ifba.cliente.repository.ClienteRepository;
import br.com.ifba.infrastructure.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService implements ClienteIService {
    private final ClienteRepository clienteRepository;

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado com ID: " + id));
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new BusinessException("CPF já está em uso por outro cliente: " + cliente.getCpf());
        }
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public Cliente delete(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado com ID: " + id));
        clienteRepository.delete(cliente);
        return cliente;
    }

    @Override
    @Transactional
    public Cliente update(Cliente cliente) {
        if (!clienteRepository.existsById(cliente.getId())) {
            throw new BusinessException("Cliente não encontrado com ID: " + cliente.getId());
        }

        clienteRepository.findByCpf(cliente.getCpf())
                .filter(c -> !c.getId().equals(cliente.getId()))
                .ifPresent(c -> {
                    throw new BusinessException("CPF já está em uso por outro cliente: " + cliente.getCpf());
                });

        return clienteRepository.save(cliente);
    }
}