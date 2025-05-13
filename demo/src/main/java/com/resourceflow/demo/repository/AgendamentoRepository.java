package com.resourceflow.demo.repository;

import com.resourceflow.demo.model.Agendamento;
import com.resourceflow.demo.model.Recurso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    // Exemplo de método de consulta personalizado:
    List<Agendamento> findByRecursoAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
            Recurso recurso, LocalDateTime dataHoraFimInicio, LocalDateTime dataHoraInicioFim);

    List<Agendamento> findByRecursoAndDataHoraInicioBetween(Recurso recurso, LocalDateTime inicio, LocalDateTime fim);

    List<Agendamento> findByRecursoAndDataHoraFimBetween(Recurso recurso, LocalDateTime inicio, LocalDateTime fim);
}