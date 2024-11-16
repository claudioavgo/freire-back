package com.freireapp.freireapp.controller;

import com.freireapp.freireapp.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aluno")
@CrossOrigin(origins = "*")
public class AlunoController {
    @Autowired
    private AlunoService alunoService;

    @GetMapping("/{id}/faltas")
    public List<Map<String, Object>> quantidadeFaltas(@PathVariable Long id) {
        return alunoService.quantidadeFaltas(id);
    }

    @GetMapping("/{id}/avaliacao")
    public List<Map<String,Object>> notasAluno(@PathVariable Long id) {
        return alunoService.notasAluno(id);
    }

    @GetMapping("/{id}/cadeiras")
    public List<Map<String,Object>> ListarCadeiras(@PathVariable Long id) {
        return alunoService.ListarCadeiras(id);
    }

    @GetMapping("/{id}/aulas-hoje")
    public List<Map<String, Object>> ListarAulasDoDia(@PathVariable Long id) {
        return alunoService.ListarAulasDoDia(id);
    }

    @GetMapping("/{id}/aulas")
    public List<Map<String, Object>> ListarAulas(@PathVariable Long id) {
        return alunoService.ListarAulas(id);
    }

    @GetMapping("{id}/streak")
    public List<Map<String, Object>> Streak(@PathVariable Long id) {
        return alunoService.Streak(id);
    }

    @GetMapping("{id}/financeiro")
    public List<Map<String, Object>> Financeiro(@PathVariable Long id) {
        return alunoService.Financeiro(id);
    }
}
