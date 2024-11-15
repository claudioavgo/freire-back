package com.freireapp.freireapp.controller;

import com.freireapp.freireapp.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aluno")
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

}
