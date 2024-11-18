package com.freireapp.freireapp.service;

import com.freireapp.freireapp.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public Map<String, Object> quantidadeFaltas(Long idAluno, Long idDisciplina) {
        return alunoRepository.getQuantidadeFaltas(idAluno, idDisciplina);
    }

    public List<Map<String, Object>> notasAluno(Long idAluno, Long idDisciplina) {
        return alunoRepository.getResultadoAvaliacao(idAluno, idDisciplina);
    }

    public List<Map<String, Object>> ListarDisciplinas(Long id) {
        return alunoRepository.getTodasDisciplinas(id);
    }

    public List<Map<String,Object>> ListarAulasDoDia(Long id) {
        String hoje = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("pt","BR"));
        String diaSemana = hoje.substring(0, 1).toUpperCase() + hoje.substring(1);
        return alunoRepository.getAulasDoDia(id, diaSemana);
    }

    public List<Map<String,Object>> ListarAulas(Long id){
        return alunoRepository.getAulas(id);
    }

    public Map<String,Object> Streak(Long id){
        return alunoRepository.getStreak(id);
    }

    public List<Map<String,Object>> Financeiro(Long id){
        return alunoRepository.getPagamentos(id);
    }

    public List<Map<String, Object>> todasNotas(Long id) {
        return alunoRepository.getTodasProvas(id);
    }
    public Map<String, Object> mediaGeral(Long id) {
        return alunoRepository.getMediaTotal(id);
    }
    public ResponseEntity pagamentoBoleto(Long id) {
        alunoRepository.pagamentoBoleto(id);
        return ResponseEntity.status(200).body("Pagamento realizado com sucesso.");
    }
}
