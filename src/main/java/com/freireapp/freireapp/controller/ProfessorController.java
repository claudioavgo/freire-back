package com.freireapp.freireapp.controller;

import com.freireapp.freireapp.dto.RegistroFaltasDTO;
import com.freireapp.freireapp.service.PessoaService;
import com.freireapp.freireapp.service.PresencaService;
import com.freireapp.freireapp.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/professores")
@CrossOrigin(origins = "*")
public class ProfessorController {

    @Autowired
    private PresencaService presencaService;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private PessoaService pessoaService;

    /*@GetMapping()
    public List<Map<String, Object>> listarProfessores() {
        return professorService.listarProfessores();
    }*/

    @PostMapping("/chamada")
    public ResponseEntity<String> registrarFaltas(@RequestBody RegistroFaltasDTO registroFaltas) {
        try {
            presencaService.registrarChamada(registroFaltas);
            return ResponseEntity.ok("Faltas registradas com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao registrar faltas.");
        }
    }

    @GetMapping("/{id}/qtd-alunos")
    public Map<String, Object> ListarQuantidadeDeAlunos(@PathVariable Long id) {
        return professorService.ListarQuantidadeDeAlunos(id);
    }

    @GetMapping("{id}/ver-alunos")
    public List<Map<String, Object>> VerAlunos(@PathVariable Long id) {
        return professorService.VerAlunos(id);
    }

    @PostMapping("/{id}/avaliacoes")
    public ResponseEntity<String> criarAvaliacao(@PathVariable Long id, @RequestBody Map<String, Object> avaliacaoData) {
        String descricao = (String) avaliacaoData.get("descricao");
        LocalDate data = LocalDate.parse((String) avaliacaoData.get("data"));

        professorService.criarAvaliacao(id, descricao, data);

        return ResponseEntity.status(201).body("Boa");
    }

}
