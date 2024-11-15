package com.freireapp.freireapp.service;

import com.freireapp.freireapp.aluno.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public List<Map<String, Object>> quantidadeFaltas(Long id) {
        return alunoRepository.getQuantidadeFaltas(id);
    }

    public List<Map<String, Object>> notasAluno(Long id) {
        return alunoRepository.getResultadoAvaliacao(id);
    }

    public List<Map<String, Object>> ListarCadeiras(Long id) {
        return alunoRepository.getTodasCadeiras(id);
    }

    public List<Map<String,Object>> ListarAulasDoDia(Long id) {
        String hoje = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("pt","BR"));
        String diaSemana = hoje.substring(0, 1).toUpperCase() + hoje.substring(1);

        System.out.println(diaSemana);
        return alunoRepository.getAulasDoDia(id, diaSemana);
    }
}
