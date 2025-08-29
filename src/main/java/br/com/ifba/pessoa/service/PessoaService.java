package br.com.ifba.pessoa.service;

import br.com.ifba.infrastructure.exception.BusinessException;
import br.com.ifba.pessoa.entity.Pessoa;
import br.com.ifba.pessoa.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PessoaService implements PessoaIService {
    private final PessoaRepository pessoaRepository;

    @Override
    public List<Pessoa> findAll() {
        return pessoaRepository.findAll();
    }

    @Override
    public Pessoa findById(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Pessoa não encontrada com ID: " + id));
    }

    @Override
    @Transactional
    public Pessoa save(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    @Override
    @Transactional
    public Pessoa delete(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Pessoa não encontrada com ID: " + id));
        pessoaRepository.delete(pessoa);
        return pessoa;
    }

    @Override
    @Transactional
    public Pessoa update(Pessoa pessoa) {
        if (!pessoaRepository.existsById(pessoa.getId())) {
            throw new BusinessException("Pessoa não encontrada com ID: " + pessoa.getId());
        }

        return pessoaRepository.save(pessoa);
    }
}