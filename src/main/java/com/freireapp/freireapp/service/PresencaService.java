package com.freireapp.freireapp.service;

import com.freireapp.freireapp.dto.RegistroFaltasDTO;
import com.freireapp.freireapp.repository.PessoaRepository;
import com.freireapp.freireapp.repository.PresencaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class PresencaService {

    @Autowired
    private PresencaRepository presencaRepository;
    @Autowired
    private PessoaRepository pessoaRepository;

    public void registrarChamada(RegistroFaltasDTO registroFaltas) {
        LocalDate data = LocalDate.now();
        for (RegistroFaltasDTO.FaltaDTO falta : registroFaltas.faltas()) {
            presencaRepository.registrarChamada(falta.idPessoa(), falta.status(), data);
        }
    }

    public List<Map<String, Object>> listarProfessores() {
        return pessoaRepository.getListaProfessores();
    }
}