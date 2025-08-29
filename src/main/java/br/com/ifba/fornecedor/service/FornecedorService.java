package br.com.ifba.fornecedor.service;

import br.com.ifba.fornecedor.entity.Fornecedor;
import br.com.ifba.fornecedor.repository.FornecedorRepository;
import br.com.ifba.infrastructure.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FornecedorService implements FornecedorIService {
    private final FornecedorRepository fornecedorRepository;

    @Override
    public List<Fornecedor> findAll() {
        return fornecedorRepository.findAll();
    }

    @Override
    public Fornecedor findById(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Fornecedor não encontrado com ID: " + id));
    }

    @Override
    @Transactional
    public Fornecedor save(Fornecedor fornecedor) {
        if (fornecedorRepository.findByCnpj(fornecedor.getCnpj()).isPresent()) {
            throw new BusinessException("CNPJ já está em uso por outro fornecedor: " + fornecedor.getCnpj());
        }
        return fornecedorRepository.save(fornecedor);
    }

    @Override
    @Transactional
    public Fornecedor delete(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Fornecedor não encontrado com ID: " + id));
        fornecedorRepository.delete(fornecedor);
        return fornecedor;
    }

    @Override
    @Transactional
    public Fornecedor update(Fornecedor fornecedor) {
        if (!fornecedorRepository.existsById(fornecedor.getId())) {
            throw new BusinessException("Fornecedor não encontrado com ID: " + fornecedor.getId());
        }

        fornecedorRepository.findByCnpj(fornecedor.getCnpj())
                .filter(f -> !f.getId().equals(fornecedor.getId()))
                .ifPresent(f -> {
                    throw new BusinessException("CNPJ já está em uso por outro fornecedor: " + fornecedor.getCnpj());
                });

        return fornecedorRepository.save(fornecedor);
    }
}