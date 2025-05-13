package com.resourceflow.demo.service;

import com.resourceflow.demo.model.Recurso;
import com.resourceflow.demo.repository.RecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecursoService {

    @Autowired
    private RecursoRepository recursoRepository;

    public List<Recurso> listarTodosRecursos() {
        return recursoRepository.findAll();
    }

    public Optional<Recurso> buscarRecursoPorId(Long id) {
        return recursoRepository.findById(id);
    }

    public Recurso criarRecurso(Recurso recurso) {
        return recursoRepository.save(recurso);
    }

    public Recurso atualizarRecurso(Long id, Recurso recursoAtualizado) {
        Optional<Recurso> recursoExistente = recursoRepository.findById(id);
        if (recursoExistente.isPresent()) {
            recursoAtualizado.setId(id);
            return recursoRepository.save(recursoAtualizado);
        }
        return null; // Ou lançar uma exceção indicando que o recurso não foi encontrado
    }

    public void deletarRecurso(Long id) {
        recursoRepository.deleteById(id);
    }

    // Outros métodos com lógica de negócios específica para recursos virão aqui
}