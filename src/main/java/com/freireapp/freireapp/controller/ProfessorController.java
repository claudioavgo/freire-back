package com.freireapp.freireapp.controller;

import com.freireapp.freireapp.dto.RegistroFaltasDTO;
import com.freireapp.freireapp.service.PessoaService;
import com.freireapp.freireapp.service.PresencaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    @Autowired
    private PresencaService presencaService;
    @Autowired
    private PessoaService pessoaService;

    @GetMapping()
    public List<Map<String, Object>> listarProfessores() {
        return presencaService.listarProfessores();
    }

    @PostMapping("/chamada")
    public ResponseEntity<String> registrarFaltas(@RequestBody RegistroFaltasDTO registroFaltas) {
        presencaService.registrarChamada(registroFaltas);
        return ResponseEntity.ok("Faltas registradas com sucesso.");
    }

}
