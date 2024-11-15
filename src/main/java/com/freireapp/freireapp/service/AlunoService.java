package com.freireapp.freireapp.service;

import com.freireapp.freireapp.aluno.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
