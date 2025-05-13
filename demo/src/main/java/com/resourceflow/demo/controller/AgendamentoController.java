package com.resourceflow.demo.controller;

import com.resourceflow.demo.model.Agendamento;
import com.resourceflow.demo.model.Recurso;
import com.resourceflow.demo.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping
    public ResponseEntity<List<Agendamento>> listarTodosAgendamentos() {
        List<Agendamento> agendamentos = agendamentoService.listarTodosAgendamentos();
        return new ResponseEntity<>(agendamentos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscarAgendamentoPorId(@PathVariable Long id) {
        Optional<Agendamento> agendamento = agendamentoService.buscarAgendamentoPorId(id);
        return agendamento.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> criarAgendamento(@RequestBody Agendamento agendamento) {
        Object resultado = agendamentoService.criarAgendamento(agendamento);
        if (resultado instanceof Agendamento) {
            return new ResponseEntity<>(resultado, HttpStatus.CREATED);
        } else if (resultado instanceof List) {
            List<Recurso> sugestoes = (List<Recurso>) resultado;
            if (!sugestoes.isEmpty()) {
                return new ResponseEntity<>(sugestoes, HttpStatus.CONFLICT); // Ou outro status adequado
            } else {
                return new ResponseEntity<>("O recurso solicitado não está disponível e não há sugestões no momento.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Erro ao processar o agendamento.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> atualizarAgendamento(@PathVariable Long id, @RequestBody Agendamento agendamentoAtualizado) {
        Agendamento agendamento = agendamentoService.atualizarAgendamento(id, agendamentoAtualizado);
        return agendamento != null ? new ResponseEntity<>(agendamento, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgendamento(@PathVariable Long id) {
        agendamentoService.deletarAgendamento(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}