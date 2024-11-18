package com.freireapp.freireapp.controller;

import com.freireapp.freireapp.dto.AvaliacaoDTO;
import com.freireapp.freireapp.dto.NotasAvaliacaoDTO;
import com.freireapp.freireapp.dto.RegistroFaltasDTO;
import com.freireapp.freireapp.repository.ProfessorRepository;
import com.freireapp.freireapp.service.PessoaService;
import com.freireapp.freireapp.service.PresencaService;
import com.freireapp.freireapp.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/professor")
@CrossOrigin(origins = "*")
public class ProfessorController {

    @Autowired
    private PresencaService presencaService;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private ProfessorRepository professorRepository;

    @GetMapping()
    public List<Map<String, Object>> listarProfessores() {
        return presencaService.listarProfessores();
    }

    @PostMapping("/chamada")
    public ResponseEntity<String> registrarFaltas(@RequestBody RegistroFaltasDTO registroFaltas) {
        try {
            presencaService.registrarChamada(registroFaltas);
            return ResponseEntity.status(200).body("Faltas registradas com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao registrar as faltas.");
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

    @PostMapping("/disciplina/{id}/avaliacao")
    public ResponseEntity<String> criarAvaliacao(@PathVariable Long id, @RequestBody AvaliacaoDTO dataAvaliacao) {
        try {
            String descricao = dataAvaliacao.descricao();
            LocalDate data = dataAvaliacao.data();
            professorService.criarAvaliacao(id, descricao, data);
            return ResponseEntity.status(201).body("Avaliação criada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar a avaliação: " + e.getMessage());
        }
    }

    @PostMapping("/avaliacao/{id}")
    public ResponseEntity notasAvaliacao(@PathVariable Long id, @RequestBody NotasAvaliacaoDTO data) {
        return ResponseEntity.status(200).body(professorService.notasAvaliacao(id,data));
    }

    @GetMapping("/{id}/disciplinas")
    public List<Map<String, Object>> listarDisciplinas (@PathVariable Long id) {
        return professorService.listarDisciplinas(id);
    }

    @GetMapping("/disciplina/{id}/alunos")
    public List<Map<String, Object>> listarAlunosPorDisciplina(@PathVariable Long id){
        return professorService.listarAlunosPorDisciplina(id);
    }

    @GetMapping("/disciplina/{id}")
    public List<Map<String, Object>> listarAvaliacao (@PathVariable Long id){
        return professorService.listarAvaliacao(id);
    }

    @GetMapping("/avaliacao/{id}/resultados")
    public List<Map<String, Object>> listarResultadosPorAvaliacao(@PathVariable Long id) {
        return professorService.listarResultadosPorAvaliacao(id);
    }

    @DeleteMapping("/avaliacao/{id}")
    public ResponseEntity<String> deletarAvaliacao(@PathVariable Long id) {
        try {
            return professorService.deletarAvaliacao(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar a avaliação.");
        }
    }

    @PutMapping("/avaliacao/{id}")
    public ResponseEntity<String> editarAvaliacao(@PathVariable Long id, @RequestBody Map<String, Object> avaliacaoData) {
        String descricao = (String) avaliacaoData.get("descricao");
        LocalDate data = LocalDate.parse((String) avaliacaoData.get("data"));

        professorService.editarAvaliacao(id, descricao, data);

        return ResponseEntity.status(200).body("Avaliação atualizada com sucesso.");
    }

    @GetMapping("/{id}/aulas-dia")
    public List<Map<String, Object>> listarAulasDoDia(@PathVariable Long id) {
        return professorService.listarAulaDoDia(id);
    }


}


