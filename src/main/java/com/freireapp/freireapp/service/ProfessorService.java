package com.freireapp.freireapp.service;

import com.freireapp.freireapp.dto.NotasAvaliacaoDTO;
import com.freireapp.freireapp.repository.AlunoRepository;
import com.freireapp.freireapp.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.*;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.List;


@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    public Map<String,Object> ListarQuantidadeDeAlunos(Long id){
        return professorRepository.getAlunos(id);
    }

    public List<Map<String, Object>> VerAlunos(Long id){
        return professorRepository.getAlunosInformacoes(id);
    }

    public void criarAvaliacao(Long idDisciplina, String descricao, LocalDate data) {
        professorRepository.criarAvaliacao(idDisciplina, descricao, data);
    }

    public ResponseEntity notasAvaliacao(Long id,NotasAvaliacaoDTO data){
        professorRepository.inserirResultadoAvaliacao(id,data);
        return ResponseEntity.status(200).body("Resultado inserido com sucesso.");
    }

    public List<Map<String, Object>> listarDisciplinas(Long id) {
        return professorRepository.listarDisciplinas(id);
    }

    public List<Map<String, Object>> listarAlunosPorDisciplina(Long idDisciplina) {
        List<Map<String, Object>> resultados = professorRepository.getAlunosPorDisciplina(idDisciplina);

        Map<Long, Map<String, Object>> alunosMap = new HashMap<>();

        for (Map<String, Object> resultado : resultados) {
            Long idAluno = ((Number) resultado.get("id_aluno")).longValue();
            Map<String, Object> alunoInfo = alunosMap.getOrDefault(idAluno, new HashMap<>());
            if (!alunosMap.containsKey(idAluno)) {
                alunoInfo.put("nome_aluno", resultado.get("nome_aluno"));
                alunoInfo.put("periodo_aluno", resultado.get("periodo_aluno"));
                alunoInfo.put("email_aluno", resultado.get("email_aluno"));
                alunoInfo.put("notas", new ArrayList<>());
                alunosMap.put(idAluno, alunoInfo);
            }
            if (resultado.get("nota_aluno") != null) {
                double notaOriginal = ((Number) resultado.get("nota_aluno")).doubleValue();
                double notaArredondada = Math.round(notaOriginal * 100.0) / 100.0;
                ((List<Double>) alunoInfo.get("notas")).add(notaArredondada);
            }
        }

        return new ArrayList<>(alunosMap.values());
    }

    public List<Map<String, Object>> listarAvaliacao (@PathVariable Long id){
        return professorRepository.listarAvaliacao(id);
    }

    public List<Map<String, Object>> listarResultadosPorAvaliacao(Long idAvaliacao) {
        return professorRepository.getResultadosPorAvaliacao(idAvaliacao);
    }

    public ResponseEntity<String> deletarAvaliacao(Long id) {
        if (!professorRepository.avaliacaoExiste(id)) {
            System.out.println("Avaliação não existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Avaliação não existe.");
        }
        professorRepository.deletarAvaliacao(id);
        return ResponseEntity.status(HttpStatus.OK).body("Avaliação deletada com sucesso.");
    }

    public void editarAvaliacao(Long idAvaliacao, String descricao, LocalDate data) {
        professorRepository.atualizarAvaliacao(idAvaliacao, descricao, data);
    }

    public List<Map<String, Object>> listarAulaDoDia(Long idProfessor) {
        return professorRepository.getAulasdoDia(idProfessor);
    }

    public int contarProvasCorrigidas(Long idProfessor) {
        return professorRepository.contarProvasCorrigidas(idProfessor);
    }


}