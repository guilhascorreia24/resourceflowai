package com.resourceflow.demo.repository;

import com.resourceflow.demo.model.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {
    // Você pode adicionar métodos de consulta personalizados aqui, se necessário
}