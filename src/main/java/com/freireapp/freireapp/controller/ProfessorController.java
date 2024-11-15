package com.freireapp.freireapp.controller;

import com.freireapp.freireapp.dto.RegistroFaltasDTO;
import com.freireapp.freireapp.service.PresencaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    @Autowired
    private PresencaService presencaService;

    @PostMapping("/chamada")
    public ResponseEntity<String> registrarFaltas(@RequestBody RegistroFaltasDTO registroFaltas) {
        presencaService.registrarChamada(registroFaltas);
        return ResponseEntity.ok("Faltas registradas com sucesso.");
    }
}
