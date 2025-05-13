package com.resourceflow.demo.repository;

import com.resourceflow.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Você pode adicionar métodos de consulta personalizados aqui, se necessário
}