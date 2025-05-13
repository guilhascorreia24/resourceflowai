package com.resourceflow.demo.controller;

import com.resourceflow.demo.model.Recurso;
import com.resourceflow.demo.service.RecursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    @Autowired
    private RecursoService recursoService;

    @GetMapping
    public ResponseEntity<List<Recurso>> listarTodosRecursos() {
        List<Recurso> recursos = recursoService.listarTodosRecursos();
        return new ResponseEntity<>(recursos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recurso> buscarRecursoPorId(@PathVariable Long id) {
        Optional<Recurso> recurso = recursoService.buscarRecursoPorId(id);
        return recurso.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Recurso> criarRecurso(@RequestBody Recurso recurso) {
        Recurso novoRecurso = recursoService.criarRecurso(recurso);
        return new ResponseEntity<>(novoRecurso, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recurso> atualizarRecurso(@PathVariable Long id, @RequestBody Recurso recursoAtualizado) {
        Recurso recurso = recursoService.atualizarRecurso(id, recursoAtualizado);
        return recurso != null ? new ResponseEntity<>(recurso, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRecurso(@PathVariable Long id) {
        recursoService.deletarRecurso(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}