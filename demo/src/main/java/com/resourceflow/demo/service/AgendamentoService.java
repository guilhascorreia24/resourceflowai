package com.resourceflow.demo.service;

import com.resourceflow.demo.model.Agendamento;
import com.resourceflow.demo.model.Recurso;
import com.resourceflow.demo.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private RecursoService recursoService;

    // Simulação de preferências do usuário (pode vir de um banco de dados no futuro)
    private static final Map<Long, String> preferenciasUsuarios = Map.of(
            1L, "Sala de Reunião Grande", // Usuário com ID 1 prefere salas grandes
            2L, "Projetor"             // Usuário com ID 2 prefere projetores
    );

    public List<Agendamento> listarTodosAgendamentos() {
        return agendamentoRepository.findAll();
    }

    public Optional<Agendamento> buscarAgendamentoPorId(Long id) {
        return agendamentoRepository.findById(id);
    }

    public Object criarAgendamento(Agendamento agendamento) {
        Recurso recursoSolicitado = agendamento.getRecurso();
        LocalDateTime inicio = agendamento.getDataHoraInicio();
        LocalDateTime fim = agendamento.getDataHoraFim();
        Long usuarioId = agendamento.getUsuario().getId(); // Assumindo que o usuário está associado ao agendamento

        if (isRecursoDisponivel(recursoSolicitado, inicio, fim)) {
            return agendamentoRepository.save(agendamento);
        } else {
            // Se o recurso não estiver disponível, buscar sugestões com preferências
            List<Recurso> sugestoes = buscarRecursosDisponiveisPorTipoECapacidadeComPreferencias(
                    recursoSolicitado.getTipo(), recursoSolicitado.getCapacidade(), inicio, fim, usuarioId);
            return sugestoes;
        }
    }

    public Agendamento atualizarAgendamento(Long id, Agendamento agendamentoAtualizado) {
        Optional<Agendamento> agendamentoExistente = agendamentoRepository.findById(id);
        if (agendamentoExistente.isPresent()) {
            agendamentoAtualizado.setId(id);
            return agendamentoRepository.save(agendamentoAtualizado);
        }
        return null;
    }

    public void deletarAgendamento(Long id) {
        agendamentoRepository.deleteById(id);
    }

    public boolean isRecursoDisponivel(Recurso recurso, LocalDateTime inicio, LocalDateTime fim) {
        List<Agendamento> conflitosInicio = agendamentoRepository.findByRecursoAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(recurso, fim, inicio);
        List<Agendamento> conflitosFim = agendamentoRepository.findByRecursoAndDataHoraInicioBetween(recurso, inicio, fim);
        List<Agendamento> conflitosDentro = agendamentoRepository.findByRecursoAndDataHoraFimBetween(recurso, inicio, fim);
        return conflitosInicio.isEmpty() && conflitosFim.isEmpty() && conflitosDentro.isEmpty();
    }

    // Novo método para buscar recursos disponíveis por tipo, capacidade e preferências do usuário
    public List<Recurso> buscarRecursosDisponiveisPorTipoECapacidadeComPreferencias(String tipoSolicitado, int capacidadeMinima, LocalDateTime inicio, LocalDateTime fim, Long usuarioId) {
        String preferenciaUsuario = preferenciasUsuarios.get(usuarioId);

        List<Recurso> sugestoesPreferenciais = recursoService.listarTodosRecursos().stream()
                .filter(recurso -> recurso.getTipo().equalsIgnoreCase(tipoSolicitado)
                        && recurso.getCapacidade() >= capacidadeMinima
                        && (preferenciaUsuario == null || recurso.getNome().equalsIgnoreCase(preferenciaUsuario)) // Priorizar se o nome corresponde à preferência (exemplo simplificado)
                        && isRecursoDisponivel(recurso, inicio, fim))
                .collect(Collectors.toList());

        List<Recurso> outrasSugestoes = recursoService.listarTodosRecursos().stream()
                .filter(recurso -> recurso.getTipo().equalsIgnoreCase(tipoSolicitado)
                        && recurso.getCapacidade() >= capacidadeMinima
                        && (preferenciaUsuario == null || !recurso.getNome().equalsIgnoreCase(preferenciaUsuario)) // Excluir os já adicionados como preferenciais
                        && isRecursoDisponivel(recurso, inicio, fim))
                .collect(Collectors.toList());

        // Combine as listas: sugestões preferenciais primeiro
        List<Recurso> sugestoesCombinadas = new ArrayList<>(sugestoesPreferenciais);
        sugestoesCombinadas.addAll(outrasSugestoes);

        return sugestoesCombinadas;
    }
}