package com.freireapp.freireapp.dto;

import java.util.List;

public record RegistroFaltasDTO(
        Long idProfessor,
        List<FaltaDTO> faltas
) {

    public static record FaltaDTO(
            Long idPessoa,
            Boolean status
    ) {
    }
}