package com.freireapp.freireapp.service;

import com.freireapp.freireapp.repository.AlunoRepository;
import com.freireapp.freireapp.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;


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
        professorRepository.inserirAvaliacao(idDisciplina, descricao, data);
    }
}