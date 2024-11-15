package com.freireapp.freireapp.service;

import com.freireapp.freireapp.dto.RegistroFaltasDTO;
import com.freireapp.freireapp.professor.PresencaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PresencaService {

    @Autowired
    private PresencaRepository presencaRepository;

    public void registrarChamada(RegistroFaltasDTO registroFaltas) {
        LocalDate data = LocalDate.now();
        for (RegistroFaltasDTO.FaltaDTO falta : registroFaltas.faltas()) {
            presencaRepository.registrarChamada(falta.idPessoa(), falta.status(), data);
        }
    }
}