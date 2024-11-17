package com.freireapp.freireapp.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public record AvaliacaoDTO(
        String descricao,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
) {
}
